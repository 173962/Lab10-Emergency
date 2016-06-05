////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Simulator core                                              //
//   #     _\  // Single, monolithic class implementing a simplistic          //
//   |   _/    // event-driven simulator.                                     //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import it.polito.tdp.emergency.model.Patient;
import it.polito.tdp.emergency.model.Patient.TriageEnum;

public class FieldHospital {
	private Queue<Event> eventQueue;
	private List<Doctor> doctor;
	private List<Assistente> assistente;
	private Queue<QueueingPatient> waitingList;
	private Map<Integer, Patient> patients;
	public Stat[] waitTriage;
	private Map<Patient, Integer> mappaDottori;
	private Map<Patient, Integer> mappaAssistenti;
	
	////////////////////////////////////////////////////////////////////////////
	// Constructor
	public FieldHospital() {
		eventQueue = new PriorityQueue<Event>();
		doctor = new ArrayList<Doctor>();
		assistente = new ArrayList<Assistente>();
		waitingList = new PriorityQueue<QueueingPatient>();
		waitTriage = new Stat[Patient.TriageEnum.values().length];
		for(int t=0; t<Patient.TriageEnum.values().length; ++t)
			waitTriage[t] = new Stat();
		mappaDottori = new HashMap<Patient, Integer>();
		mappaAssistenti = new HashMap<Patient, Integer>();
	}

	////////////////////////////////////////////////////////////////////////////
	// Setter
	public void setPatients(Map<Integer,Patient> p) {
		patients = p;
	}

	////////////////////////////////////////////////////////////////////////////
	// Reset stat
	public void reset() {
		eventQueue = new PriorityQueue<Event>();
		waitingList = new PriorityQueue<QueueingPatient>();
		waitTriage = new Stat[Patient.TriageEnum.values().length];
		for(int t=0; t<Patient.TriageEnum.values().length; ++t)
			waitTriage[t] = new Stat();
		mappaDottori = new HashMap<Patient, Integer>();
		mappaAssistenti = new HashMap<Patient, Integer>();
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Adding & Getting events
	public void addDoctor(String name) {
		doctor.add(new Doctor(name));
	}
	public void addEvent(Event e) {
		//System.out.print("==> SCHEDULED: " + e + "\n");
		eventQueue.add(e);
	}	
	public void addEvent(long time, Event.EventEnum type, int data) {
		addEvent(new Event(time, type, data));
	}
	public void addAssistente(String name) {
		assistente.add(new Assistente(name));
	}
		
	////////////////////////////////////////////////////////////////////////////
	// Main function
	public boolean moreEvents() {
		return !eventQueue.isEmpty();
	}
	
	public int processNextEvent() {
		Event e = eventQueue.remove();
		
		switch(e.type) {
		case DOCTOR_INIZIA_TURNO:
			System.out.println(e.getTag() + " " + "-- " + doctor.get(e.data) + " inizia turno");
			doctor.get(e.data).iniziaTurno();
			addEvent(e.time+8*60*60*1000, Event.EventEnum.DOCTOR_FINISCE_TURNO, e.data);
			break;
			
		case DOCTOR_FINISCE_TURNO:
			System.out.println(e.getTag() + " " + "-- " + doctor.get(e.data) + " finisce turno");
			doctor.get(e.data).finisceTurno();
			if(!waitingList.isEmpty())
				addEvent(e.time+16*60*60*1000, Event.EventEnum.DOCTOR_INIZIA_TURNO, e.data);
			break;
			
		case ASSISTENTE_INIZIA_TURNO:
			System.out.println(e.getTag() + " " + "-- " + assistente.get(e.data) + " inizia turno");
			assistente.get(e.data).iniziaTurno();
			addEvent(e.time+8*60*60*1000, Event.EventEnum.ASSISTENTE_FINISCE_TURNO, e.data);
			break;
			
		case ASSISTENTE_FINISCE_TURNO:
			System.out.println(e.getTag() + " " + "-- " + assistente.get(e.data) + " finisce turno");
			assistente.get(e.data).finisceTurno();
			if(!waitingList.isEmpty())
				addEvent(e.time+16*60*60*1000, Event.EventEnum.ASSISTENTE_INIZIA_TURNO, e.data);
			break;
		
		case PATIENT_ARRIVAL:		// Someone to take care of
			System.out.println(e.getTag() + " " + patients.get(e.data) + " arrives.");
			// Schedule patient's death
			switch(patients.get(e.data).getTriageE()) {
			case RED:
				// Need advanced medical care at once
				addEvent(e.time+1*60*60*1000, Event.EventEnum.PATIENT_DEATH, e.data);
				break;
			case YELLOW:
				// Stable condition, but require medical assistance
				addEvent(e.time+6*60*60*1000, Event.EventEnum.PATIENT_DEATH, e.data);
				break;
			case GREEN:
				// No need for advanced medical care for at least several hours
				addEvent(e.time+12*60*60*1000, Event.EventEnum.PATIENT_DEATH, e.data);
				break;
			case WHITE:
				// Walking wounded: he'll be fine...
			case BLACK:
				// Walking dead: too late.
				break;
			default:
				throw new IllegalArgumentException("Unknow triage type: " + patients.get(e.data).getTriage());
			}
			waitingList.add(new QueueingPatient(patients.get(e.data), e.time));
			break;
			
		case PATIENT_RECOVERY:		
			if(patients.get(e.data).getStatusE() != Patient.StatusEnum.DEAD) {
				double x = Math.random();
				
				if(patients.get(e.data).getTriageE() == TriageEnum.RED && x < 0.3) {
					addEvent(e.time, Event.EventEnum.PATIENT_DEATH_DURING_RECOVERY, e.data);
				}
				else if(patients.get(e.data).getTriageE() == TriageEnum.YELLOW && x < 0.12) {
					addEvent(e.time, Event.EventEnum.PATIENT_DEATH_DURING_RECOVERY, e.data);
				}
				else if(patients.get(e.data).getTriageE() == TriageEnum.GREEN && x < 0.02) {
					addEvent(e.time, Event.EventEnum.PATIENT_DEATH_DURING_RECOVERY, e.data);
				}
				else {
					patients.get(e.data).setStatus("Safe");
					patients.get(e.data).setTriage("White");
					System.out.println(e.getTag() + " " + patients.get(e.data).getName() + " recovers!");
				}
			}
			break;
			
		case PATIENT_DEATH:			// Bad news
			if(patients.get(e.data).getStatusE() == Patient.StatusEnum.INJURED) {
				patients.get(e.data).setStatus("Dead");
				patients.get(e.data).setTriage("Black");
				System.out.println(e.getTag() + " " + patients.get(e.data).getName() + " passed away.");
				
				if(mappaAssistenti.containsKey(patients.get(e.data)))
					addEvent(e.time, Event.EventEnum.ASSISTENTE_AVAILABLE, e.data);
				else if(mappaDottori.containsKey(patients.get(e.data)))
					addEvent(e.time, Event.EventEnum.DOCTOR_AVAILABLE, e.data);
			} 
			break;
		
		case PATIENT_DEATH_DURING_RECOVERY:
			if(patients.get(e.data).getTriageE() == TriageEnum.GREEN)
				patients.get(e.data).setStatus("Dead_During_Recovery_Green");
			if(patients.get(e.data).getTriageE() == TriageEnum.YELLOW)
				patients.get(e.data).setStatus("Dead_During_Recovery_Yellow");
			if(patients.get(e.data).getTriageE() == TriageEnum.RED)
				patients.get(e.data).setStatus("Dead_During_Recovery_Red");
			
			patients.get(e.data).setTriage("Black");
			System.out.println(e.getTag() + " " + patients.get(e.data).getName() + " passed away during recovery.");
			
			if(mappaAssistenti.containsKey(patients.get(e.data)))
				addEvent(e.time, Event.EventEnum.ASSISTENTE_AVAILABLE, e.data);
			else if(mappaDottori.containsKey(patients.get(e.data)))
				addEvent(e.time, Event.EventEnum.DOCTOR_AVAILABLE, e.data);
			else
				throw new IllegalArgumentException("Unknown patient-doctor or patient-assistent assignation: " + patients.get(e.data));
			
			break;
		
		case DOCTOR_AVAILABLE:
			if(mappaDottori.containsKey(patients.get(e.data))) {
				System.out.println(e.getTag() + " " + doctor.get(mappaDottori.get(patients.get(e.data))) + " becomes available.");
				doctor.get(mappaDottori.get(patients.get(e.data))).endTreatment();
				mappaDottori.remove(patients.get(e.data));
			}
			break;
		
		case ASSISTENTE_AVAILABLE:
			if(mappaAssistenti.containsKey(patients.get(e.data))) {
				System.out.println(e.getTag() + " " + assistente.get(mappaAssistenti.get(patients.get(e.data))) + " becomes available.");
				assistente.get(mappaAssistenti.get(patients.get(e.data))).endTreatment();
				mappaAssistenti.remove(patients.get(e.data));
			}
			break;
			
		default:
			throw new IllegalArgumentException("Unknow event type: " + e);
		}
		
		// Let's do some work
		treatPatients(e.time);
		
		return 1;
	}
	
	protected boolean treatPatients(long now) {
		int md;
		long treatTime;
		while( (md = findAvailabeDoctor()) !=-1 ) {
			while(waitingList.peek()!=null && patients.get(waitingList.peek().id).getStatusE()!=Patient.StatusEnum.INJURED) {
				waitingList.remove();
			}
			if(waitingList.isEmpty())
				return false;			// whoa! nothing to do
			QueueingPatient qp = waitingList.remove();
			waitTriage[qp.triage.ordinal()].addData(now-qp.arrivalTime); // Some stat
			Patient p = patients.get(qp.id);
			
			System.out.println(Event.getTag(now) + " Assigning " + p.getName() + " to " + doctor.get(md));
			doctor.get(md).startTreatment();
			switch(p.getTriageE()) {
			case RED:
				// need advanced medical care at once or within 1 hour
				treatTime = 30*60*1000;
				break;
			case YELLOW:
				// stable condition but require medical assistance
				treatTime = 30*60*1000;
				break;
			case GREEN:
				// not need advanced medical care for at least several hours
				treatTime = 30*60*1000;
				break;
			case WHITE:
				// Walking wounded, he'll be fine...
				treatTime = 30*60*1000;
				break;
			default:
				throw new IllegalArgumentException("Unknow triage: " + p.getTriage());
			}
			mappaDottori.put(p, md);
			addEvent(now + treatTime/2, Event.EventEnum.PATIENT_RECOVERY, p.getId());
			//addEvent(now + treatTime, Event.EventEnum.DOCTOR_AVAILABLE, md);
			addEvent(now + treatTime, Event.EventEnum.DOCTOR_AVAILABLE, p.getId());
		}
		
		while( (md = findAvailabeAssistente()) !=-1 ) {
			while(waitingList.peek()!=null && patients.get(waitingList.peek().id).getStatusE()!=Patient.StatusEnum.INJURED) {
				waitingList.remove();
			}
			if(waitingList.isEmpty())
				return false;			// whoa! nothing to do
			QueueingPatient qp = waitingList.peek();
			Patient p = patients.get(qp.id);
			if(p.getTriageE() != TriageEnum.RED) {
				waitTriage[qp.triage.ordinal()].addData(now-qp.arrivalTime); // Some stat
				waitingList.remove();
				System.out.println(Event.getTag(now) + " Assigning " + p.getName() + " to " + assistente.get(md));
				assistente.get(md).startTreatment();
				switch(p.getTriageE()) {
				case YELLOW:
					// stable condition but require medical assistance
					treatTime = 30*60*1000;
					break;
				case GREEN:
					// not need advanced medical care for at least several hours
					treatTime = 30*60*1000;
					break;
				case WHITE:
					// Walking wounded, he'll be fine...
					treatTime = 30*60*1000;
					break;
				default:
					throw new IllegalArgumentException("Unknow triage: " + p.getTriage());
				}
				mappaAssistenti.put(p, md);
				addEvent(now + treatTime/2, Event.EventEnum.PATIENT_RECOVERY, p.getId());
				//addEvent(now + treatTime, Event.EventEnum.ASSISTENTE_AVAILABLE, md);
				addEvent(now + treatTime, Event.EventEnum.ASSISTENTE_AVAILABLE, p.getId());
			}
			else
				break;
		}
		return true;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Doctors
	protected int findAvailabeDoctor() {
		for(int t=0; t<doctor.size(); ++t) 
			if(doctor.get(t).isAvailable())
				return t;
		return -1;
	}
	
	protected int findAvailabeAssistente() {
		for(int t=0; t<assistente.size(); ++t) 
			if(assistente.get(t).isAvailable())
				return t;
		return -1;
	}
}
