package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.DepartamentoDAO;
import com.entities.Departamento;
import com.repositories.Repository;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class DepartamentoService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = Repository.class, beanName="DepartamentoDAO")
	private Repository<Departamento> departamentoDAO;

	public List<Departamento> obtenerTodosDepartamento() {
		return departamentoDAO.getAll();
	}

	public Departamento obtenerPorIdDepartamento(long idDepartamento) {
		return departamentoDAO.get(idDepartamento);
	}
}
