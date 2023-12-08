package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.entities.Evento;
import com.entities.Semestre;
import com.exceptions.ServiciosException;
import com.repositories.Repository;
import com.repositories.RepositoryEvento;
import com.repositories.RepositorySemestre;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class SemestreDAO implements RepositorySemestre {
	@PersistenceContext
	protected EntityManager em;

	@Override
	public Semestre get(long id) {
		return em.find(Semestre.class, id);
	}

	@Override
	public List<Semestre> getAll() {
		TypedQuery<Semestre> query = em.createQuery("SELECT u FROM Semestre u ORDER BY u.idSemestre ASC",
				Semestre.class);
		return query.getResultList();
	}
	
	@Override
	public void save(Semestre t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Semestre");
		}
	}
	
	@Override
	public void update(Semestre t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Semestre t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public List<Semestre> getByFiltro(Evento evento) {
	    CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<Semestre> configConsulta = cb.createQuery(Semestre.class);
	    Root<Semestre> raizSemestre = configConsulta.from(Semestre.class);

	    configConsulta.select(raizSemestre);

	    List<Predicate> predicates = new ArrayList<Predicate>();

	    if (evento != null) {
	        // Añade un predicado que compruebe si el evento está presente en la lista de eventos del semestre.
	        predicates.add(cb.isMember(evento, raizSemestre.get("eventos")));
	    }

	    configConsulta.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

	    return em.createQuery(configConsulta).getResultList();
	}
}
