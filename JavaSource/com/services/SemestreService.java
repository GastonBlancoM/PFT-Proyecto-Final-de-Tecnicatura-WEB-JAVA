package com.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.entities.Evento;
import com.entities.Semestre;
import com.exceptions.ServiciosException;
import com.repositories.RepositorySemestre;

import lombok.Data;

@Data
@Stateless
@LocalBean
public class SemestreService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB private RepositorySemestre dao;
	
	public List<Semestre> obtenerTodos(){
		return dao.getAll();
	}
	
	public List<Semestre> obtenerPorFiltros(Evento evento){
		return dao.getByFiltro(evento);
	}
	
	public Semestre buscarPorId(long id) {
		return dao.get(id);
	}
 	
	public void save(Semestre semestre) throws ServiciosException {
		dao.save(semestre);
	}
}
