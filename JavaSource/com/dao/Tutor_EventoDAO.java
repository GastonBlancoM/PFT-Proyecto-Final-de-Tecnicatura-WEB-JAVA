package com.dao;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;


import com.entities.Evento;
import com.entities.Tutor_Evento;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryTutor_Evento;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class Tutor_EventoDAO implements RepositoryTutor_Evento{
@PersistenceContext private EntityManager em;
	

	public void save(Tutor_Evento t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Tutor_Evento");
		}
	}
	
	public void delete(Evento t) throws ServiciosException {
		try {
			for (Tutor_Evento te : getAll()) {
				if(te.getIdEvento().equals(t)) {
					em.remove(te);
					em.flush();
				}
			}
		
		
			
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Tutor_Evento");
		}
	}
	
	public List<Tutor_Evento> getAll() {
		TypedQuery<Tutor_Evento> query = em.createQuery("SELECT u FROM Tutor_Evento u",
				Tutor_Evento.class);
		return query.getResultList();
	}
	
	@Override
	public List<Tutor_Evento> findByEvento(Evento evento) {
		TypedQuery<Tutor_Evento> query = em.createQuery("SELECT u FROM Tutor_Evento u WHERE u.idEvento = :idEvento",
				Tutor_Evento.class);
		query.setParameter("idEvento", evento);
		return query.getResultList();
	}

	
	public Tutor_Evento get(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void update(Tutor_Evento t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}


	public void delete(Tutor_Evento t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	
	

	
	
	
//	public List<Tutor_Evento> findByEvento(Evento evento) {
//	    try {
//	        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//	        CriteriaQuery<Tutor_Evento> criteriaQuery = criteriaBuilder.createQuery(Tutor_Evento.class);
//	        Root<Tutor_Evento> tutorEventoRoot = criteriaQuery.from(Tutor_Evento.class);
//
//	        // Añadir condiciones al criterio (en este caso, equivalente a WHERE u.idEvento = :idEvento)
//	        criteriaQuery.where(criteriaBuilder.equal(tutorEventoRoot.get("idEvento"), evento));
//
//	        // Crear y ejecutar la consulta
//	        TypedQuery<Tutor_Evento> query = em.createQuery(criteriaQuery);
//	        return query.getResultList();
//	    } catch (Exception e) {
//	        // Manejar excepciones según tus necesidades
//	        e.printStackTrace();
//	        return Collections.emptyList(); // O devolver null, lanzar una excepción personalizada, etc.
//	    }
//	}
}
