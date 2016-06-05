////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Arrival bean                                                //
//   #     _\  // Describes the arrival of one patient in the field hospital  //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.model;

import java.sql.Timestamp;

public class Arrival {
	private Timestamp time;
	private String triage;
	private int patient;
	
	////////////////////////////////////////////////////////////////////////////
	// Getters
	public Timestamp getTime() {
		return time;
	}
	public String getTriage() {
		return triage;
	}
	public int getPatient() {
		return patient;
	}
	
	public Arrival(Timestamp time, int patient, String triage) {
		this.patient = patient;
		this.time = time;
		this.triage = triage;
	}

	@Override
	public String toString() {
		return patient + " [" + triage + "] @ " + time;
	}
}
