////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Patient bean                                                //
//   #     _\  // Describes one patient.                                      //
//   |   _/    // Note: current status is different from triage               //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.model;

public class Patient {
	////////////////////////////////////////////////////////////////////////////
	// ENUMS
	public enum TriageEnum { RED, YELLOW, GREEN, WHITE, BLACK };
	public enum StatusEnum { SAFE, INJURED, DEAD, DEAD_DURING_RECOVERY_GREEN, DEAD_DURING_RECOVERY_YELLOW, DEAD_DURING_RECOVERY_RED };

	private int id;
	protected String name;
	protected TriageEnum triage;
	protected StatusEnum status;

	////////////////////////////////////////////////////////////////////////////
	// Canonical hash & equals
	@Override
	public int hashCode() {	
		long hash = id * 2654435761L;	// dr. knuth magic number
		return (int)hash;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Getters & Setters
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status.toString();
	}
	public StatusEnum getStatusE() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	public void setStatus(String status) {
		// valueOf() would have been simpler, but CaSe SeNsItIvE
		if(status.compareToIgnoreCase("Safe")==0)
			this.status = StatusEnum.SAFE;
		else if(status.compareToIgnoreCase("Injured")==0)
			this.status = StatusEnum.INJURED;
		else if(status.compareToIgnoreCase("Dead")==0)
			this.status = StatusEnum.DEAD;
		else if(status.compareToIgnoreCase("Dead_During_Recovery_Green")==0)
			this.status = StatusEnum.DEAD_DURING_RECOVERY_GREEN;
		else if(status.compareToIgnoreCase("Dead_During_Recovery_Yellow")==0)
			this.status = StatusEnum.DEAD_DURING_RECOVERY_YELLOW;
		else if(status.compareToIgnoreCase("Dead_During_Recovery_Red")==0)
			this.status = StatusEnum.DEAD_DURING_RECOVERY_RED;
		else
			throw new ClassCastException("Unknown status: \"" + status + "\"");
	}	
	public String getTriage() {
		return triage.toString();
	}
	public TriageEnum getTriageE() {
		return triage;
	}
	public void setTriage(TriageEnum triage) {
		this.triage = triage;
	}
	public void setTriage(String triage) {
		// valueOf() would have been simpler, but CaSe SeNsItIvE
		if(triage.compareToIgnoreCase("Black")==0)
			this.triage = TriageEnum.BLACK;
		else if(triage.compareToIgnoreCase("Red")==0)
			this.triage = TriageEnum.RED;
		else if(triage.compareToIgnoreCase("Yellow")==0)
			this.triage = TriageEnum.YELLOW;
		else if(triage.compareToIgnoreCase("Green")==0)
			this.triage = TriageEnum.GREEN;
		else if(triage.compareToIgnoreCase("White")==0)
			this.triage = TriageEnum.WHITE;
		else
			throw new ClassCastException("Unknown triage: \"" + triage + "\"");
	}

	////////////////////////////////////////////////////////////////////////////
	// Constructor
	public Patient() {
	}
	public Patient(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.status = StatusEnum.SAFE;
		this.triage = TriageEnum.WHITE;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Spare utilities
	@Override
	public String toString() {
		return name + " (#" + id + ", " + status + ", " + triage + ")";
	}
}
