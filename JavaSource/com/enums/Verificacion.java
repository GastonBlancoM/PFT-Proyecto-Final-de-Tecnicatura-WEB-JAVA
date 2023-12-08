package com.enums;

public enum Verificacion {
VERIFICADO("Verificado", 1), NO_VERIFICADO("No verificado", 2);
	
	private String nombre;
	private int id;
	
	private Verificacion(String nombre, int id) {
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
