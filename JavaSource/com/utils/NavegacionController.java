package com.utils;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import javax.inject.Named;

import com.enums.TipoUsuario;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "navController")
public class NavegacionController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean estudiante;
	private boolean tutor;
	private boolean analista;
	private String nomUsuLogueado;

	public String irLogin() {
		return "login_page.xhtml?faces-redirect=true";
	}

	public String irRegistro() {
		return "register_page.xhtml?faces-redirect=true";
	}
	
	public void AlternVista(TipoUsuario tipo, String nomUsuLogueado) {
		this.estudiante = TipoUsuario.ESTUDIANTE.equals(tipo);
		this.analista = TipoUsuario.ANALISTA.equals(tipo);
		this.tutor = TipoUsuario.TUTOR.equals(tipo);
		
		this.nomUsuLogueado = nomUsuLogueado;
	}
	
}
