package com.filtros;


import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.enums.TipoUsuario;
import com.utils.CookiesUtil;
import com.utils.JwtUtil;

@WebFilter("/sitio/*")
public class SessionExpirationFilter implements Filter{
	@Inject private RouteAccessManager routeAccessManager;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Cookie cookie = null;
        try {
            cookie = CookiesUtil.getCookie("jwt", httpRequest);
            } catch (TokenExpiredException e) {
    			e.printStackTrace();
    		} catch (JWTVerificationException e) {
    			e.printStackTrace();
    		}
            
            if (cookie == null) {
            	httpResponse.sendRedirect(httpRequest.getContextPath() + "/login_page.xhtml?faces-redirect=true");
            }
            try {
            	JwtUtil.isValidJwt(cookie.getValue());
    		} catch (TokenExpiredException e) {
    			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login_page.xhtml?faces-redirect=true");
    			
    		} catch (JWTVerificationException e) {
    			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login_page.xhtml?faces-redirect=true");
    		}
            
        	
        	TipoUsuario tipo = JwtUtil.getTipoUsuarioFromJwt(cookie.getValue());
    		String uri = httpRequest.getRequestURI().toString().replace("/sitio", "");
    		try {
    			if (routeAccessManager.check(uri, tipo)) {
    				chain.doFilter(request, response);
    			} else {
    				httpResponse.sendRedirect(httpRequest.getContextPath() + "/sitio/acceso_denegado.xhtml?faces-redirect=true");
    			}
    		} catch (Exception e) {
    			httpResponse.sendRedirect(httpRequest.getContextPath() + "/sitio/pag_Bienvenida.xhtml?faces-redirect=true");
    		}
	}
	

	
	
}
