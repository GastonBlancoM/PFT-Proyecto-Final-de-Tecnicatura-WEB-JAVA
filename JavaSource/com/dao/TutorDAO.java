package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.client.GestionUsuario;
import com.entities.Tutor;
import com.exceptions.ServiciosException;


@Stateless
public class TutorDAO {
	@PersistenceContext
	private EntityManager em;
	
	private GestionUsuario gestionUsuario = new GestionUsuario();

	/**
	 * Default constructor.
	 */
	public TutorDAO() {
		// TODO Auto-generated constructor stub

	}

	public Tutor crear(Tutor tut) throws ServiciosException {
		try {
			em.persist(tut);
			em.flush();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro de tutor correcto",
					"El tutor se registro correctamente, este usuario debe ser verificado para poder logearse.");

			gestionUsuario.infoMessage(message);

			return tut;
		} catch (PersistenceException e) {
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de registro",
					"El tutor no se pudo registrar, Asegúrate de que los datos ingresados son correctos o inténtalo más tarde.");

			gestionUsuario.infoMessage(message);
//			
			throw new ServiciosException("No se pudo crear el tutor");
		
		}

	}

	public void modificar(Tutor tut) throws ServiciosException {
		try {
			em.merge(tut);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario tutor modificado",
					"El usuario tutor fue modificado correctamente.");

			gestionUsuario.infoMessage(message);
			
		} catch (PersistenceException e) {
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR en la modificación",
					"Asegúrate de que los datos ingresados del tutor son correctos.");

			gestionUsuario.infoMessage(message);
			
			throw new ServiciosException("No se pudo modificar el Tutor");
		}

	}

	public void borrar(Long id) {
		try {
			Tutor tut = em.find(Tutor.class, id);
			em.remove(tut);
			em.flush();
			
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			
			
		}
	}

	public List<Tutor> obtenerTodos() {
		TypedQuery<Tutor> query = em.createQuery("SELECT u FROM Tutor u ORDER BY u.idUsuario ASC",
				Tutor.class);
		return query.getResultList();
	}

	public Tutor buscarTutor(Long id) {
		Tutor tut = em.find(Tutor.class, id);
		return tut;
	}

}
