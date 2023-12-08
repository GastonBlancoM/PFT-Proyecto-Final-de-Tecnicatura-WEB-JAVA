package com.enums;

public enum EstadoEstudiante {
MATRICULADO("Matriculado", 1), NO_MATRICULADO("No Matriculado", 2), CONGELADO("Congelado",3);
	
	private String nombre;
	private int id;
	
	private EstadoEstudiante(String nombre, int id) {
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
