package com.cauadev.autenticacaojwt.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cauadev.autenticacaojwt.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class JwtService {
	
	@Value("${security.jwt.expiracao}")
	private String expiracao;
	
	@Value("${security.jwt.chave-assinatura}")
	private String assinatura;
	
	Claims obterClaims(String token) throws ExpiredJwtException {
		return  Jwts.parser()
				.setSigningKey(assinatura)
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String obterLoginUsuario(String token) throws ExpiredJwtException{
		return (String) obterClaims(token).getSubject();
	}
	
	public boolean tokenValido(String token) {
		try {
			Claims claims = obterClaims(token);
			Date date = claims.getExpiration();
			LocalDateTime localDateTime =
					date.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			
			return !LocalDateTime.now().isAfter(localDateTime);
		} catch (Exception e) {
			return false;
		}
	}
	
	public String generateToken(Usuario usuario) {
		long exp = Long.valueOf(expiracao);
		LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(exp);
		
		Date data = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		
		return Jwts
				.builder()
				.setSubject(usuario.getUsuario())
				.setExpiration(data)
				.signWith(SignatureAlgorithm.HS512, assinatura)
				.compact();
	}
	
	

}
