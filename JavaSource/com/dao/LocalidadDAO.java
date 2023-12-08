package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.entities.Departamento;
import com.entities.Localidad;
import com.exceptions.ServiciosException;
import com.repositories.LocalidadRepository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class LocalidadDAO implements LocalidadRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Localidad> getAll() {
		TypedQuery<Localidad> query = em.createQuery("SELECT u FROM Localidad u ORDER BY u.idLocalidad ASC", Localidad.class);
		return query.getResultList();
	}
	
	@Override
	public void save(Localidad t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Localidad t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Localidad t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Localidad> getByDepartamento(Departamento departamento) {
		TypedQuery<Localidad> query = em.createQuery("SELECT u FROM Localidad u WHERE u.departamento = :departamento", Localidad.class)
				.setParameter("departamento", departamento);
		return query.getResultList();
	}
		
	@Override
	public Localidad getByNombre(String nombre) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();		
			CriteriaQuery<Localidad> configConsulta = cb.createQuery(Localidad.class);
			
			Root<Localidad> raizLocalidad = configConsulta.from(Localidad.class);
			
			configConsulta.select(raizLocalidad);
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			predicates.add(cb.equal(raizLocalidad.get("nombre"), nombre));
			
			configConsulta.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
			return em.createQuery(configConsulta).getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Localidad get(long id) {
		return em.find(Localidad.class, id);
	}
}
