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
import com.entities.Estudiante;
import com.exceptions.ServiciosException;

@Stateless
public class EstudianteDAO {
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private GestionUsuario gestionUsuario;

	/**
	 * Default constructor.
	 */
	public EstudianteDAO() {
		// TODO Auto-generated constructor stub

	}

	public Estudiante crear(Estudiante est) throws ServiciosException {
		try {
			em.persist(est);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro de estudiante correcto",
					"El estudiante se registro correctamente, este usuario debe ser verificado para poder logearse.");

			gestionUsuario.infoMessage(message);
			
			return est;
		} catch (PersistenceException e) {
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de registro",
					"El estudiante no se pudo registrar, Asegúrate de que los datos ingresados son correctos o inténtalo más tarde.");

			gestionUsuario.infoMessage(message);
			
			throw new ServiciosException("No se pudo crear el Estudiante");
		}

	}

	public void modificar(Estudiante est) throws ServiciosException {
		try {
			em.merge(est);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario estudiante modificado",
					"El usuario fue modificado correctamente.");

			gestionUsuario.infoMessage(message);
			
		} catch (PersistenceException e) {
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR en la modificación",
					"Asegúrate de que los datos ingresados del estudiante son correctos.");

			gestionUsuario.infoMessage(message);
			
			throw new ServiciosException("No se pudo modificar el Estudiante");
		}

	}

	public void borrar(Long id) {
		try {
			Estudiante est = em.find(Estudiante.class, id);
			em.remove(est);
			em.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}
	
	public Estudiante buscarEstudiante(Long id) {
		Estudiante est = em.find(Estudiante.class, id);
		return est;
	}
	
	public List<Estudiante> obtenerTodos() {
		TypedQuery<Estudiante> query = em.createQuery("SELECT u FROM Estudiante u ORDER BY u.idUsuario ASC",
				Estudiante.class);
		return query.getResultList();
	}
}
