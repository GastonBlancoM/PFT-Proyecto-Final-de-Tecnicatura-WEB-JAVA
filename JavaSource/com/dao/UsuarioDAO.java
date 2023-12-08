package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.entities.Usuario;
import com.enums.EstadoUsuario;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryUsuario;
import com.utils.MessagesUtil;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class UsuarioDAO implements RepositoryUsuario {
	@PersistenceContext
	protected EntityManager em;

	@Override
	public void save(Usuario t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Usuario");
		}
		
	}


	@Override
	public void update(Usuario t) throws ServiciosException {
		try {
			em.merge(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar el usuario");
		}
		
	}


	@Override
	public void delete(Usuario t) throws ServiciosException {
		try {			
			em.remove(t);
			em.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void deleteLogical(long id) throws ServiciosException {
		try {
			Usuario usu = em.find(Usuario.class, id);
			usu.setEstadoUsuario(EstadoUsuario.INACTIVO);
			em.merge(usu);
			em.flush();
			MessagesUtil.createInfoMessage("Usuario borrado con éxito", "La baja logica fue aplicada correctamente.");

		} catch (PersistenceException e) {
			e.printStackTrace();

			MessagesUtil.createErrorMessage("ERROR al borrar usuario",
					"La baja logica baja logica no se pudo aplicar, asegúrate de que no existan errores en la operación.");

		}
	}


	@Override
	public Usuario get(long id) {
		return em.find(Usuario.class, id);
	}

	@Override
	public List<Usuario> getAll(){
		TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u ORDER BY u.idUsuario ASC", Usuario.class);
		return query.getResultList();
	}
	
	@Override
	public void changePassword(long id, String pass) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Usuario getByNombreUsuario(String nombre) {
		try {
		TypedQuery<Usuario> query = em
				.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nom", Usuario.class)
				.setParameter("nom", nombre);
		return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	public Usuario getByMailInstitucional(String mail) {
		try {
			TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.mailInstitucional = :mail", Usuario.class)
					.setParameter("mail", mail);
			return query.getSingleResult();			
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	public Usuario getByDocumento(int documento) {
		try {
			TypedQuery<Usuario> query = em
					.createQuery("SELECT u FROM Usuario u WHERE u.documento = :documento", Usuario.class)
					.setParameter("documento", documento);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	public List<Usuario> getByFilter(long id, String nombre, int documento) {
		return null;
	}



}
