package com.enums;

public enum TipoUsuario {
	ANALISTA("Analista", 1), ESTUDIANTE("Estudiante", 2), TUTOR("Tutor", 3);

	private String nombre;
	private int id;

	private TipoUsuario(String nombre, int id) {
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
