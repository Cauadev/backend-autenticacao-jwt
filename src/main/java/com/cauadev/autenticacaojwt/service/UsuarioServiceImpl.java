package com.cauadev.autenticacaojwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cauadev.autenticacaojwt.exceptions.SenhaInvalidaException;
import com.cauadev.autenticacaojwt.model.Usuario;
import com.cauadev.autenticacaojwt.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public UserDetails autenticar(Usuario usuario) {
		UserDetails user = loadUserByUsername(usuario.getUsuario());
		boolean senha = encoder.matches(usuario.getSenha(), user.getPassword());
		if(senha) {
			return user;
		}
		throw new SenhaInvalidaException();
	}
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		return repository.save(usuario);
	}

	@Override
	public UserDetails loadUserByUsername(String user){
		Usuario usuario = repository.findByUsuario(user)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário "+user+" não encontrado."));
		
		String[] roles = usuario.isAdmin() ? new String[] {"ADMIN","USER"} : new String[] {"USER"};
		
		return User
				.builder()
				.roles(roles)
				.username(usuario.getUsuario())
				.password(usuario.getSenha())
				.build();
	}
	
	

}
