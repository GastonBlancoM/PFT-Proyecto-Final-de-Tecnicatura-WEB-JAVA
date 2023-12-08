package com.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departamento implements Serializable{
	private static final long serialVersionUID = 372899516509590991L;

	@Id
	private long idDepartamento;
	
	private String nombre;

	
}
