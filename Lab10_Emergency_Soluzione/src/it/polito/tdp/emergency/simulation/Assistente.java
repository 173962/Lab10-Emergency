package it.polito.tdp.emergency.simulation;

public class Assistente {
	private boolean available;
	private String name;
	
	private boolean turno;
	
	public Assistente() {
		available = true;
		name = "John Doe (0x" + this.toString() + ")";
		turno = true;
	}
	
	public Assistente(String name) {
		available = true;
		turno = true;
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
			throw new IllegalStateException("Assistente not available");
		//System.out.print(name + " M.D. is now busy.\n");
		available = false;
	}
	public void endTreatment() {
		if(available) 
			throw new IllegalStateException("Assistente already available");
		//System.out.print(name + " M.D. is now available.\n");
		available = true;
	}
	@Override
	public String toString() {
		return name + " M.D.";
	}
}
