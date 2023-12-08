package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Reclamo;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryReclamo;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class ReclamoDAO implements RepositoryReclamo {
	@PersistenceContext private EntityManager em;
	
	@Override
	public Reclamo get(long id) {
		return em.find(Reclamo.class, id);
	}
	
	@Override
	public List<Reclamo> getAll() {
		TypedQuery<Reclamo> query = em.createQuery("SELECT u FROM Reclamo u ORDER BY u.idReclamo ASC", Reclamo.class);
		return query.getResultList();
	}
	
	@Override
	public void save(Reclamo t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el reclamo");
		}
	}
	
	@Override
	public void update(Reclamo t) throws ServiciosException {
		try {
			em.merge(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar el Reclamo");
		}
	}
	
	@Override
	public void delete(Reclamo t) throws ServiciosException {
		try {
			t = em.merge(t);
			em.remove(t);
			em.flush();
		} catch (EntityNotFoundException e) {
			throw new ServiciosException(e.getMessage());
		} catch (NoResultException e) {
			throw new ServiciosException(e.getMessage());
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}
	
	@Override
	public List<Reclamo> getByEstudiante(Estudiante estudiante) {
		TypedQuery<Reclamo> query = em.createQuery("SELECT i FROM Reclamo i WHERE i.estudiante = :estudiante", Reclamo.class)
				.setParameter("estudiante", estudiante);
		return query.getResultList();
	}
	
	public List<Reclamo> getByEvento(Evento evento) {
		TypedQuery<Reclamo> query = em.createQuery("SELECT i FROM Reclamo i WHERE i.evento = :evento", Reclamo.class)
				.setParameter("evento", evento);
		return query.getResultList();
	}
}
