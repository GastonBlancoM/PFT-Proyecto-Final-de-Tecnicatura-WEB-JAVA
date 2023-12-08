package com.filtros;


import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.enums.TipoUsuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
@Named(value = "routeAccessManager")
public class RouteAccessManager {
	
private HashMap<String, List<TipoUsuario>> map = new HashMap<String, List<TipoUsuario>>();;
	
	@PostConstruct
	public void init() {
		new PermissionAccess(map, "/sitio/general/perfil/PerfilUser.xhtml?faces-redirect=true").addAll();
		new PermissionAccess(map, "/sitio/pag_Bienvenida.xhtml?faces-redirect=true").addAll();
		new PermissionAccess(map, "/sitio/analista/listaITR's.xhtml?faces-redirect=true").addAnalista();
		new PermissionAccess(map, "/sitio/analista/listaEventos.xhtml?faces-redirect=true").addAnalista();
		new PermissionAccess(map, "/sitio/analista/listaUsuarios.xhtml?faces-redirect=true").addAnalista();
		new PermissionAccess(map, "/sitio/analista/listaEventos.xhtml?faces-redirect=true").addTutor().addAnalista();
//		new PermissionAccess(map, "/reportes/reclamos/bi").addTutor().addAnalista();
//		new PermissionAccess(map, "/convocatorias").addAll();
		new PermissionAccess(map, "/sitio/analista/listaJustificaciones.xhtml?faces-redirect=true").addEstudiante().addAnalista();
//		new PermissionAccess(map, "/reclamos").addEstudiante().addAnalista();
	}
	
	public boolean check(String path, TipoUsuario t) throws Exception {
		List<TipoUsuario> allowed = map.get(path);
		if (allowed == null) throw new Exception("No existe la ruta ingresada");
		return allowed.contains(t);
	}
}
