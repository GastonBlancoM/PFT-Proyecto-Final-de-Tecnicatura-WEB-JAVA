package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.Departamento;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class DepartamentoDAO implements Repository<Departamento>{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Departamento> getAll() {
		TypedQuery<Departamento> query = em.createQuery("SELECT u FROM Departamento u ORDER BY u.idDepartamento ASC", Departamento.class);
		return query.getResultList();
	}
	
	@Override
	public Departamento get(long id) {
		return em.find(Departamento.class, id);
	}
	
	@Override
	public void save(Departamento t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Departamento t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Departamento t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}
	
	public Departamento obtenerPorNombre(String nom) {
		TypedQuery<Departamento> query = em.createQuery("SELECT i FROM Departamento i WHERE i.nombre = :nom", Departamento.class)
				.setParameter("nom", nom);
		return query.getSingleResult();
	}
}
