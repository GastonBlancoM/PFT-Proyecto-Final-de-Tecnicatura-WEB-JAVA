package com.enums;

public enum EstadoEvento {
	FINALIZADO("Finalizado", 1), CORRIENTE("Corriente", 2), FUTURO("Futuro", 3);

	private String nombre;
	private int id;

	private EstadoEvento(String nombre, int id) {
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
