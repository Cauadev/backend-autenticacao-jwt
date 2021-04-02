package com.cauadev.autenticacaojwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cauadev.autenticacaojwt.model.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Integer>{
	
	Optional<Usuario> findByUsuario(String usuario);

}
