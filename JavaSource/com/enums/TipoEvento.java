package com.enums;

public enum TipoEvento {
	EXAMEN("Examen",1),
	JORNADA_PRESENCIAL("Jornada Presencial",2),
	PRUEBA_FINAL("Prueba Final",3),
	DEFENSA_DE_PROYECTO("Defensa de Proyecto",4),
	EVENTO_VME("Evento VME",5),
	ACTIVIDAD_OPTATIVA("Actividad Optativa",6),
	ACTIVIDAD_APE("Actividad APE",7);

	private String nombre;
	private int id;

	private TipoEvento(String nombre, int id) {
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
