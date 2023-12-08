package com.rest;

import com.entities.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
	
	private String token;
	private Usuario usuario;
	
}
