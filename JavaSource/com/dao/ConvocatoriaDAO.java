package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.entities.Convocatoria;
import com.entities.Estudiante;
import com.entities.Evento;

import com.exceptions.ServiciosException;
import com.repositories.Repository;
import com.repositories.RepositoryConvocatoria;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class ConvocatoriaDAO implements RepositoryConvocatoria<Convocatoria> {
	@PersistenceContext
	protected EntityManager em;

	@Override
	public Convocatoria get(long id) {
		return em.find(Convocatoria.class, id);
	}

	@Override
	public void save(Convocatoria c) throws ServiciosException {
		try {
			em.persist(c);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear la Convocatoria");
		}
	}

	@Override
	public void update(Convocatoria c) throws ServiciosException {
		try {
			em.merge(c);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar la Convocatoria");
		}
	}

	@Override
	public void delete(Convocatoria c) throws ServiciosException {
		try {
			c = em.merge(c);
			em.remove(c);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo eliminar la Estudiante de la  convocatoria");
		}
	}

	@Override
	public List<Convocatoria> getAll() {

		TypedQuery<Convocatoria> query = em.createQuery("SELECT u FROM Convocatoria u ORDER BY u.idConvocatoria ASC",
				Convocatoria.class);
		return query.getResultList();
	}
	
	
	public List<Convocatoria> getByEstudiante(Estudiante estudiante) {

		TypedQuery<Convocatoria> query = em.createQuery("SELECT u FROM Convocatoria u WHERE u.estudiante = :estudiante",
				Convocatoria.class);
		query.setParameter("estudiante", estudiante);
		return query.getResultList();
	}

	public List<Convocatoria> getByEvento(Evento evento) {
		TypedQuery<Convocatoria> query = em.createQuery("SELECT u FROM Convocatoria u WHERE u.evento = :evento",
				Convocatoria.class);
		query.setParameter("evento", evento);
		return query.getResultList();
	}

	public List<Convocatoria> getByFilter(Estudiante estudiante) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Convocatoria> configConsulta = cb.createQuery(Convocatoria.class);

		Root<Convocatoria> raizConvocatoria = configConsulta.from(Convocatoria.class);

		// raizConvocatoria.join("IDEVENTO", JoinType.LEFT);

		configConsulta.select(raizConvocatoria);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (estudiante != null) {
			predicates.add(cb.equal(raizConvocatoria.get("estudiante"), estudiante));
		}

		configConsulta.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

		return em.createQuery(configConsulta).getResultList();
	}
		
	public List<Convocatoria> getFilterByEstudentEvent(Estudiante estudiante, Evento evento) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Convocatoria> configConsulta = cb.createQuery(Convocatoria.class);

		Root<Convocatoria> raizConvocatoria = configConsulta.from(Convocatoria.class);

		// raizConvocatoria.join("IDEVENTO", JoinType.LEFT);

		configConsulta.select(raizConvocatoria);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (estudiante != null) {
			predicates.add(cb.equal(raizConvocatoria.get("estudiante"), estudiante));
		}
		
		if(evento !=null) {
			predicates.add(cb.equal(raizConvocatoria.get("evento"), evento));
		}
		configConsulta.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

		return em.createQuery(configConsulta).getResultList();
	}
}
