////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Doctor                                                      //
//   #     _\  // Utility class for handling a doctor (no specific roles)     //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.simulation;

public class Doctor {
	private boolean available;
	private String name;
	
	private boolean turno;
	
	public Doctor() {
		available = true;
		name = "John Doe (0x" + this.toString() + ")";
		turno = false;
	}
	
	public Doctor(String name) {
		available = true;
		turno = false;
		this.name = name;
	}
	
	public void iniziaTurno() {
		turno = true;
	}
	
	public void finisceTurno() {
		turno = false;
	}
	
	public boolean isAvailable() {
		return available && turno;
	}
	public void startTreatment() {
		if(!available) 
			throw new IllegalStateException("Doctor not available");
		//System.out.print(name + " M.D. is now busy.\n");
		available = false;
	}
	public void endTreatment() {
		if(available) 
			throw new IllegalStateException("Doctor already available");
		//System.out.print(name + " M.D. is now available.\n");
		available = true;
	}
	@Override
	public String toString() {
		return name + " M.D.";
	}
}
