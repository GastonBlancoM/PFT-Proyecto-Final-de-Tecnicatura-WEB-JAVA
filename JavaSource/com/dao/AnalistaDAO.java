package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.client.GestionUsuario;
import com.entities.Analista;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class AnalistaDAO implements Repository<Analista> {
	@PersistenceContext
	private EntityManager em;
	
	@Override
    public void save(Analista t) throws ServiciosException {
        try {
            em.persist(t);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServiciosException("No se pudo crear el Analista");
        }
    }

	 @Override
	    public void update(Analista t) throws ServiciosException {
	        try {
	            em.merge(t);
	            em.flush();
	        } catch (PersistenceException e) {
	            throw new ServiciosException("No se pudo modificar el Analista");
	        }
	    }

	 @Override
	    public void delete(Analista t) throws ServiciosException {
	        try {
	            em.remove(t);
	            em.flush();
	        } catch (PersistenceException e) {
	            e.printStackTrace();
	        }
	    }

	public List<Analista> getAll() {
		TypedQuery<Analista> query = em.createQuery("SELECT u FROM Analista u ORDER BY u.idUsuario ASC",
				Analista.class);
		return query.getResultList();
	}

	@Override
    public Analista get(long id) {
        return em.find(Analista.class, id);
    }

}
