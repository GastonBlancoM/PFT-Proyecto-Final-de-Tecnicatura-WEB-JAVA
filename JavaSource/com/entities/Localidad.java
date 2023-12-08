package com.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Localidad {
	@Id
	private long idLocalidad;
	
	private String nombre;
	
	@ManyToOne
	@JoinColumn(name = "IDDEPARTAMENTO")
	private Departamento departamento;

	
}
