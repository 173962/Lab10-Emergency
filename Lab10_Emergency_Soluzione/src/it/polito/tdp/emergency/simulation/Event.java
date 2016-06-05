////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Event descriptor                                            //
//   #     _\  // Describe a single event in the simulator                    //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.simulation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public class Event implements Comparable<Event> {
	public enum EventEnum {
		PATIENT_ARRIVAL, PATIENT_RECOVERY, PATIENT_DEATH,
		DOCTOR_AVAILABLE, DOCTOR_INIZIA_TURNO, DOCTOR_FINISCE_TURNO,
		ASSISTENTE_AVAILABLE, ASSISTENTE_INIZIA_TURNO, ASSISTENTE_FINISCE_TURNO,
		PATIENT_DEATH_DURING_RECOVERY
	}
	
	protected long time;
	protected EventEnum type;
	protected int data;
	
	public Event(long time, EventEnum type, int data) {
		super();
		this.time = time;
		this.type = type;
		this.data = data;
	}

	static public String getTag(long time) {
		NumberFormat fmt = new DecimalFormat("00");	
		Date d = new Date(time);
		return "[" + fmt.format(d.getDate()) + "-" + fmt.format(1+d.getMonth()) + "-" + fmt.format(1900+d.getYear())
				+ " " + fmt.format(d.getHours()) + ":" + fmt.format(d.getMinutes()) + ":" + fmt.format(d.getSeconds()) + "]";
	}
	public String getTag() {
		return getTag(time);
	}
	
	@Override
	public String toString() {
		Date d = new Date(time);
		return type.toString() + "(" + data + ") @ " + d.toString();
	}

	@Override
    public int compareTo(Event x) {
		return Long.compare(time, x.time);
	}
}
