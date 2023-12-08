package com.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.entities.Usuario;
import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;
import com.services.UsuarioService;
import com.utils.CookiesUtil;
import com.utils.JwtUtil;
import com.utils.MessagesUtil;
import com.utils.NavegacionController;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "gestionAutenticacion")
public class GestionAutenticacion implements Serializable{
private static final long serialVersionUID = 1L;
	
	private Usuario usuarioSeleccionado;

	private String nombreUsuarioLogin;
	private String contraseniaLogin;

	private String tipoUsuarioLog;
	
	@Inject	private UsuarioService usuarioService;
	
	@Inject	private GestionUsuario gestionUsuario;
	@Inject private NavegacionController navControl;

	@PostConstruct
	public void init() {
	}

	public String login() {
		Usuario usuario = usuarioService.login(nombreUsuarioLogin, contraseniaLogin);
		if(usuario != null) {
			navControl.AlternVista(usuario.getTipoUsuario(), usuario.getNombre()+" "+usuario.getApellido());
			limpiarCamposLogin();
			return "/sitio/pag_Bienvenida.xhtml?faces-redirect=true";
		}
			
		return null;
		
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
	    session.invalidate();
		CookiesUtil.setCookie("jwt", "", 0);
		return "/login_page.xhtml?faces-redirect=true";
	}
	
	public String loadNombreUsuarioLogeado() {
		String jwt = CookiesUtil.getCookie("jwt");
		return JwtUtil.getNombreUsuarioFromJwt(jwt);
	}
	
	public long loadIdUsuarioLogeado() {
		String jwt = CookiesUtil.getCookie("jwt");
		return JwtUtil.getIdFromJwt(jwt);
	}
	
	public TipoUsuario loadTipoUsuarioLogeado() {
		String jwt = CookiesUtil.getCookie("jwt");
		return JwtUtil.getTipoUsuarioFromJwt(jwt);
	}
	
	public void limpiarCamposLogin() {
		nombreUsuarioLogin = "";
		contraseniaLogin = "";
	}
}
