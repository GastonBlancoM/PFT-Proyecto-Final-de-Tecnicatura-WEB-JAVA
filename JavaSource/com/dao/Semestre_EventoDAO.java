package com.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.entities.Semestre_Evento;
import com.exceptions.ServiciosException;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class Semestre_EventoDAO {
	@PersistenceContext private EntityManager em;
	

	public void save(Semestre_Evento t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Semestre_Evento");
		}
	}
	
	
}
