package com.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.entities.Usuario;
import com.enums.EstadoUsuario;
import com.enums.Verificacion;
import com.services.UsuarioService;
import com.utils.HashingUtil;
import com.utils.JwtUtil;


@Path("/auth")
public class AuthRecurso {
	@Inject
	private UsuarioService usuarioService;
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginSolicitud request) {
		try {
			Usuario usuario = usuarioService.obtenerUsuarioPorNombreUsuario(request.getUsername());
			
			if (usuario == null ) {
				System.out.println("Error primer if");
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
//			if (!HashingUtil.verify(request.getPassword(), usuario.getContrasenia())) {
//				return Response.status(Response.Status.UNAUTHORIZED).build();
//			}
			
			if (Verificacion.VERIFICADO != usuario.getVerificacion()) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			
			if (Verificacion.VERIFICADO != usuario.getVerificacion()) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			
			if (EstadoUsuario.ACTIVO != usuario.getEstadoUsuario()) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			
			String token = JwtUtil.createJwt(usuario.getNombreUsuario(), usuario.getTipoUsuario(), usuario.getIdUsuario());
			
			return Response.ok(new AuthResponse(token, usuario)).build();
			
		} catch (Exception e) {
			System.out.println("Error catch");
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	
}
