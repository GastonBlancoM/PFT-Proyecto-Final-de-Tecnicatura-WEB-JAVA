package com.repositories;

import java.util.List;

import com.entities.Usuario;
import com.exceptions.ServiciosException;


public interface RepositoryUsuario extends Repository<Usuario> {
	void deleteLogical(long id) throws ServiciosException;

	void changePassword(long id, String pass) throws ServiciosException;
	
	Usuario getByNombreUsuario(String nombre);
	
	Usuario getByMailInstitucional(String mail);
	
	Usuario getByDocumento(int documento);
	
	List<Usuario> getByFilter(long id, String nombre, int documento);
}
