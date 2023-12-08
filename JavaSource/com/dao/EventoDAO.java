package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.entities.Convocatoria;
import com.entities.Evento;
import com.entities.Justificacion;
import com.entities.Reclamo;
import com.entities.Tutor;
import com.entities.Tutor_Evento;
import com.entities.Usuario;
import com.enums.EstadoEvento;
import com.enums.Modalidad;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryEvento;
import com.services.ConvocatoriaService;
import com.services.JustificacionService;
import com.services.ReclamoService;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class EventoDAO implements RepositoryEvento {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Evento get(long id) {

		return em.find(Evento.class, id);

	}

	@Override
	public List<Evento> getAll() {
		TypedQuery<Evento> query = em.createQuery("SELECT e FROM Evento e", Evento.class);
		return query.getResultList();
	}

	@Override
	public List<Evento> getEventosConTutores() {
		TypedQuery<Evento> query = em.createQuery("SELECT e FROM Evento e JOIN FETCH e.listaTutores", Evento.class);
		return query.getResultList();
	}

	@Override
	public void save(Evento t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Evento");
		}
	}

	@Override
	public void update(Evento t) throws ServiciosException {
		try {
			em.merge(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar el Evento");
		}
	}

	@Override
	public void delete(Evento t) throws ServiciosException {
		try {
			em.remove(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo eliminar el evento");
		}

	}

	@Override
	public void deleteLogical(Evento t) throws ServiciosException {
		try {
			t.setEstadoEvento(EstadoEvento.FINALIZADO);
			em.merge(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo realizar la baja lógica");
		}

	}

	public List<Evento> getByFiltro(EstadoEvento estado, Modalidad modalidad) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Evento> configConsulta = cb.createQuery(Evento.class);

		Root<Evento> raizEvento = configConsulta.from(Evento.class);

		configConsulta.select(raizEvento);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (estado != null) {
			predicates.add(cb.equal(raizEvento.get("estadoEvento"), estado));
		}

		if (modalidad != null) {
			predicates.add(cb.equal(raizEvento.get("modalidad"), modalidad));
		}

		configConsulta.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

		return em.createQuery(configConsulta).getResultList();
	}

//	public List<Evento> getByTutor(Tutor tutor) {
//		TypedQuery<Evento> query = em
//				.createQuery("SELECT i FROM Evento i JOIN FETCH i.listaTutores WHERE i.tutor = :tutor", Evento.class);
//		query.setParameter("tutor", tutor);
//		return null;
//	}
	@Override
	public List<Evento> getByTutor(Tutor tutor) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Evento> criteriaQuery = criteriaBuilder.createQuery(Evento.class);
		Root<Evento> eventoRoot = criteriaQuery.from(Evento.class);
		Join<Evento, Tutor> tutorJoin = eventoRoot.join("listaTutores");

		criteriaQuery.select(eventoRoot).where(criteriaBuilder.equal(tutorJoin.get("idUsuario"), tutor.getIdUsuario()));

		return em.createQuery(criteriaQuery).getResultList();
	}
}
