package com.enums;

public enum Modalidad {
	PRESENCIAL ("Presencial",1),
	VIRTUAL ("Virtual",2),
	HIBRIDO ("Hibrido",3);
	
	private String nombre;
	private int id;
	
	private Modalidad (String nombre, int id) {
		this.nombre = nombre;
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	public int getId() {
		return id;
	}
}
