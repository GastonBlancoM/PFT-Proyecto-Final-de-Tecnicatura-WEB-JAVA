package com.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Semestre implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private long idSemestre;
	
	private String nombre;
}

