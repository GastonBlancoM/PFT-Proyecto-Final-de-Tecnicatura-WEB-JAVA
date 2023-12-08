package com.utils;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookiesUtil {
	
	public static void setCookie(String cookieName, String cookieValue, int maxAge) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");

		cookie.setMaxAge(maxAge);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addCookie(cookie);
	}
	
	public static String getCookie(String cookieName) {
		Map<String, Object> cookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
		Cookie cookie = (Cookie) cookies.get(cookieName);
		if (cookie == null) return null;
		return cookie.getValue();
	}
	
	public static Cookie getCookie(String cookieName, HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies == null) return null;
		for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
            	return JwtUtil.isValidJwt(cookie.getValue()) ? cookie : null;
            }
        }
		return null;
	}
}
