package com.utils;

import java.util.Date;
import java.util.UUID;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enums.TipoUsuario;

//Este es un Service
@Stateless
@LocalBean
public class JwtUtil {

	private static final String jwtKey = "This is an ultra secret key";
	private static Algorithm algorithm = Algorithm.HMAC256(jwtKey);

	public static String createJwt(String username, TipoUsuario tipoUsuario, long idUsuario) {
		Long currentTime = System.currentTimeMillis();
		String jwt = JWT.create().withIssuer("SYSSTEM").withSubject(username).withClaim("idUsuario", idUsuario)
				.withClaim("tipoUsuario", tipoUsuario.name()).withIssuedAt(new Date(currentTime))
				.withExpiresAt(new Date(currentTime + 3600000)).withJWTId(UUID.randomUUID().toString()).sign(algorithm);
		return jwt;
	}// 3600000 milisegundos = 1 hora

	public static boolean isValidJwt(String jwt) {
		JWTVerifier verifier = JWT.require(algorithm).withIssuer("SYSSTEM").build();
		try {
			verifier.verify(jwt);
			return true;
		} catch (JWTVerificationException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public static String getNombreUsuarioFromJwt(String jwt) {
		try {
			DecodedJWT decodedJWT = getDecodedFromJwt(jwt);
			return decodedJWT.getSubject().toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static Long getIdFromJwt(String jwt) {
		try {
			DecodedJWT decodedJWT = getDecodedFromJwt(jwt);
			return decodedJWT.getClaim("idUsuario").asLong();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static TipoUsuario getTipoUsuarioFromJwt(String jwt) {
		try {
			DecodedJWT decodedJWT = getDecodedFromJwt(jwt);
			System.out.println(decodedJWT.getClaim("tipoUsuario").asString());
			return TipoUsuario.valueOf(decodedJWT.getClaim("tipoUsuario").asString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private static DecodedJWT getDecodedFromJwt(String jwt) throws JWTVerificationException {
		JWTVerifier verifier = JWT.require(algorithm).withIssuer("SYSSTEM").build();
		DecodedJWT decodedJWT = verifier.verify(jwt);
		return decodedJWT;
	}

	public static long getTimeUntilTokenExpiration(String jwt) {
		try {
			DecodedJWT decodedJWT = getDecodedFromJwt(jwt);
			Date expirationDate = decodedJWT.getExpiresAt();
			long currentTime = System.currentTimeMillis();
			return expirationDate.getTime() - currentTime;
		} catch (Exception e) {
			return 0;
		}
	}
}