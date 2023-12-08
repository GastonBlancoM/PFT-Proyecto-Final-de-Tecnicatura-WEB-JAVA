package com.enums;

public enum RolTutor {
ENCARGADO("Encargado", 1), TUTOR("Tutor", 2);
	
	private String nombre;
	private int id;
	
	private RolTutor(String nombre, int id) {
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
