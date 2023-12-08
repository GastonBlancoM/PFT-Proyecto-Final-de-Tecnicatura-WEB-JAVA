package com.dto;

import java.util.List;

import com.entities.Evento;

import lombok.Data;


@Data
public class SemestreDTO {
	private long idSemestre;

	private String nombre;

	private List<Evento> eventos;
	
	
}
