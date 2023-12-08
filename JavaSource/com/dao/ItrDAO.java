package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.entities.Itr;
import com.enums.EstadoItr;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.NoArgsConstructor;


@Stateless
@NoArgsConstructor
public class ItrDAO implements Repository<Itr> {
	
	@PersistenceContext	private EntityManager em;

	@Override
	public Itr get(long id) {
		return em.find(Itr.class, id);
	}

	@Override
	public List<Itr> getAll() {
		TypedQuery<Itr> query = em.createQuery("SELECT u FROM Itr u ORDER BY u.idItr ASC",
				Itr.class);
		return query.getResultList();
	}

	@Override
	public void save(Itr t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Itr");
		}
	}

	@Override
	public void update(Itr t) throws ServiciosException {
		try {
			em.merge(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar el Itr");
		}
	}

	@Override
	public void delete(Itr t) throws ServiciosException {
		try {
			t.setEstadoItr(EstadoItr.DESACTIVADO);
			em.merge(t);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}
}