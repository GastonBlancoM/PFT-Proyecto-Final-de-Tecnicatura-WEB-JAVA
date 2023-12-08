package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.DepartamentoDAO;
import com.dao.LocalidadDAO;
import com.entities.Departamento;
import com.entities.Localidad;
import com.repositories.LocalidadRepository;

import lombok.Data;
import java.io.Serializable;

//Este es un Service
@Data
@Stateless
@LocalBean
public class LocalidadService implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EJB
	private LocalidadRepository localidadDAO;
	
	public List<Localidad> obtenerTodosCarreras(){
		return localidadDAO.getAll();
	}
	public List<Localidad> obtenerLocalidadesPorDepartamento(Departamento departamento) {
		return localidadDAO.getByDepartamento(departamento);
	}

	public Localidad obtenerPorNombreLocalidad(String nombre) {
		return localidadDAO.getByNombre(nombre);
	}
	
	public Localidad buscarLocalidadPorId (long id) {
		return localidadDAO.get(id);
	}
	
}
