////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // QueueingPatient                                             //
//   #     _\  // Reference to a patient in a queue. Include comparator.      //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.simulation;

import it.polito.tdp.emergency.model.Patient;
import it.polito.tdp.emergency.model.Patient.TriageEnum;

public class QueueingPatient implements Comparable<QueueingPatient> 
{
	public long arrivalTime;
	public int id;
	public TriageEnum triage;

	public QueueingPatient() {
		
	}
	
	public QueueingPatient(Patient patient, long arrivalTime) {
		super();
		id = patient.getId();
		triage = patient.getTriageE();
		this.arrivalTime = arrivalTime;
	}
	
	@Override
    public int compareTo(QueueingPatient x)
    {
		if(triage != x.triage)
			return triage.compareTo(x.triage);
		else
			return Long.compare(arrivalTime, x.arrivalTime);
    }
}
