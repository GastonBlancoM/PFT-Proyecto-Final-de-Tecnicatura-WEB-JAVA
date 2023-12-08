package com.enums;

public enum EstadoJustificacion{
	
	INGRESADO("INGRESADO",1),
	EN_PROCESO("EN PROCESO",2),
	FINALIZADO("FINALIZADO",3);
	
	private String nombre;
	private int id;

	private EstadoJustificacion(String nombre, int id) {
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
