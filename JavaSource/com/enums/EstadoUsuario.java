package com.enums;

public enum EstadoUsuario {
ACTIVO("Activo", 1), INACTIVO("Inactivo", 2);
	
	private String nombre;
	private int id;
	
	private EstadoUsuario(String nombre, int id) {
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
