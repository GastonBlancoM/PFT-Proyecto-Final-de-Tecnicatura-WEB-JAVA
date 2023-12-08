package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import com.dto.AccionJustificacionDTO;
import com.dto.JustificacionDTO;
import com.entities.AccionJustificacion;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Justificacion;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryJustificacion;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class JustificacionDAO implements RepositoryJustificacion{
	@PersistenceContext
	protected EntityManager em;

	@Override
	public void save(Justificacion j) throws ServiciosException {
		em.persist(j);
		em.flush();
	}

	@Override
	public void update(Justificacion j) throws ServiciosException {
		em.merge(j);
		em.flush();

	}

	@Override
	public void delete(Justificacion j) throws ServiciosException {
		em.remove(j);
		em.flush();

	}

	@Override
	public AccionJustificacion accionCreate(AccionJustificacion accion, Justificacion justificacion) throws ServiciosException {
		try {
			em.persist(accion);
			em.merge(justificacion);
			em.flush();
			return accion;
		} catch (EntityExistsException e) {
			throw new ServiciosException(e.getMessage());
		} catch (RollbackException e) {
			throw new ServiciosException(e.getMessage());
		}
	}
	
	@Override
	public Justificacion get(long id) {
		return em.find(Justificacion.class, id);

	}

	@Override
	public List<Justificacion> getAll() {
		TypedQuery<Justificacion> query = em.createQuery("SELECT i FROM Justificacion i", Justificacion.class);
		return query.getResultList();
	}

	@Override
	public List<Justificacion> getByEstudiante(Estudiante estudiante) {
		TypedQuery<Justificacion> query = em
				.createQuery("SELECT i FROM Justificacion i WHERE i.estudiante = :estudiante", Justificacion.class);
		query.setParameter("estudiante", estudiante);
		return query.getResultList();
	}

	@Override
	public List<Justificacion> getByEvento(Evento evento) {
		TypedQuery<Justificacion> query = em.createQuery("SELECT i FROM Justificacion i WHERE i.evento = :evento",
				Justificacion.class);
		query.setParameter("evento", evento);
		return query.getResultList();
	}

	@Override
	public List<AccionJustificacion> getAccionByJustificacion(Justificacion justificacion) {
		TypedQuery<AccionJustificacion> query = em.createQuery(
				"SELECT a FROM AccionJustificacion a WHERE a.justificacion = :justificacion ORDER BY a.idAccion",
				AccionJustificacion.class);
		query.setParameter("justificacion", justificacion);
		return query.getResultList();
	}

}