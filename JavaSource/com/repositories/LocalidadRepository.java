package com.repositories;

import java.util.List;

import com.entities.Departamento;
import com.entities.Localidad;

public interface LocalidadRepository extends Repository<Localidad> {
	Localidad getByNombre(String nombre);

	List<Localidad> getByDepartamento(Departamento departamento);
}
