package com.enums;

public enum TipoReclamo {
	EVENTO_VME("Evento VME",1),
	ACTIVIDAD_APE("Actividad APE",2);
	
	private String nombre;
	private int id;

	private TipoReclamo(String nombre, int id) {
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
