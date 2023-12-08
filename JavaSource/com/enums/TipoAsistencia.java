package com.enums;

public enum TipoAsistencia {
	ASISTENCIA("Asistencia",1),
	MEDIA_ASISTENCIA_MATUTINA("Media Asistencia Matutina",2),
	MEDIA_ASISTENCIA_VESPERTINA("Media Asistencia Vespertina",3),
	MEDIA_ASISTENCIA_AUSENCIA("Media Asistencia Ausencia",4),
	AUSENCIA_JUSTIFICADA("Ausencia Justificada",5),
	AUSENCIA("Ausencia",6);

	private String nombre;
	private int id;

	private TipoAsistencia(String nombre, int id) {
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
