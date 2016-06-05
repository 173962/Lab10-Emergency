package it.polito.tdp.emergency.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.emergency.db.FieldHospitalDAO;
import it.polito.tdp.emergency.simulation.Event;
import it.polito.tdp.emergency.simulation.FieldHospital;

public class Model {

	FieldHospital hospital = null;
	List<Integer> sfasamentiDottori = null;
	List<Integer> sfasamentiAssistenti = null;

	public Model() {
		hospital = new FieldHospital();
		sfasamentiDottori = new ArrayList<Integer>();
		sfasamentiAssistenti = new ArrayList<Integer>();
	}

	public void addDottore(String nome, int sfasamento) {
		hospital.addDoctor(nome);
		sfasamentiDottori.add(sfasamento);
		System.out.println("dottor " + nome + " aggiunto!");
	}

	public void addAssistente(String nome, int sfasamento) {
		hospital.addAssistente(nome);
		sfasamentiAssistenti.add(sfasamento);
		System.out.println("assistente " + nome + " aggiunto!");
	}

	public String startSimulation() {
		hospital.reset();
		FieldHospitalDAO dao = new FieldHospitalDAO();

		////////////////////////////////////////////////////////////////////////////
		// Kickstart: read all patients
		Map<Integer, Patient> patientsMap = dao.GetAllPatients();

		////////////////////////////////////////////////////////////////////////////
		// Kickstart: parse events
		List<Arrival> arrivals = dao.GetAllArrivals();

		long firstTimeArrival = Long.MAX_VALUE;
		
		// Iterate over the arrival list
		for(Arrival arrival : arrivals) {
			
			long time = arrival.getTime().getTime();

			if (time < firstTimeArrival)
				firstTimeArrival = time;

			int patient = arrival.getPatient();
			patientsMap.get(patient).setStatus("Injured");
			patientsMap.get(patient).setTriage(arrival.getTriage());
			hospital.addEvent(time, Event.EventEnum.PATIENT_ARRIVAL, patient);
		}

		// finally: set patients data
		hospital.setPatients(patientsMap);

		////////////////////////////////////////////////////////////////////////////
		// Let's roll!
		for (int i = 0; i < sfasamentiDottori.size(); i++) {
			hospital.addEvent(firstTimeArrival + sfasamentiDottori.get(i) * 60 * 60 * 1000,
					Event.EventEnum.DOCTOR_INIZIA_TURNO, i);
		}

		for (int i = 0; i < sfasamentiAssistenti.size(); i++) {
			hospital.addEvent(firstTimeArrival + sfasamentiAssistenti.get(i) * 60 * 60 * 1000,
					Event.EventEnum.ASSISTENTE_INIZIA_TURNO, i);
		}

		System.out.print("\n\n*** SIMULATION STARTS ***\n\n");
		int max = 0;
		while (hospital.moreEvents() && max < 20000) {
			++max;
			hospital.processNextEvent();
		}

		////////////////////////////////////////////////////////////////////////////
		// Let's roll!
		System.out.print("\n\n*** SIMULATION ENDS ***\n\n");
		int bodyCount = 0;
		int bodyCountDuringRecoveryGreen = 0;
		int bodyCountDuringRecoveryRed = 0;
		int bodyCountDuringRecoveryYellow = 0;
		int totPatients = 0;
		
		for (Patient patient : patientsMap.values()) {
			++totPatients;
			if (patient.getStatusE() == Patient.StatusEnum.DEAD) {
				++bodyCount;
			} else if (patient.getStatusE() == Patient.StatusEnum.DEAD_DURING_RECOVERY_GREEN) {
				++bodyCountDuringRecoveryGreen;
			} else if (patient.getStatusE() == Patient.StatusEnum.DEAD_DURING_RECOVERY_YELLOW) {
				++bodyCountDuringRecoveryYellow;
			} else if (patient.getStatusE() == Patient.StatusEnum.DEAD_DURING_RECOVERY_RED) {
				++bodyCountDuringRecoveryRed;
			}
		}

		String output;
		output = "Death toll: " + (bodyCount + bodyCountDuringRecoveryGreen + bodyCountDuringRecoveryYellow
				+ bodyCountDuringRecoveryRed) + "/" + totPatients;
		output += "\n";
		output += "Death during recovery GREEN: " + bodyCountDuringRecoveryGreen;
		output += "\n";
		output += "Death during recovery YELLOW: " + bodyCountDuringRecoveryYellow;
		output += "\n";
		output += "Death during recovery RED: " + bodyCountDuringRecoveryRed;
		output += "\n";
		output += "Statistics for treated patients:";
		output += "\n";
		for (int t = 0; t < 4; ++t) {
			output += "* " + hospital.waitTriage[t].getNum() + " " + Patient.TriageEnum.values()[t]
					+ " patients. Waiting time: ";
			output += " " + niceTime(hospital.waitTriage[t].getMin()) + " (min)";
			output += ";";
			output += " " + niceTime(hospital.waitTriage[t].getMax()) + " (max)";
			output += ";";
			output += " " + niceTime(hospital.waitTriage[t].getAverage()) + " (avg)";
			output += "\n";
		}

		return output;
	}

	static private String niceTime(double time) {
		long min = (long) Math.rint(time / 1000.0 / 60.0);
		long days = min / 60 / 24;
		min -= days * 60 * 24;
		long hours = min / 60;
		min -= hours * 60;

		if (days > 0) {
			return days + "d " + hours + "h " + min + "'";
		} else if (hours > 0) {
			return hours + "h " + min + "'";
		} else {
			return min + "'";
		}
	}
}
