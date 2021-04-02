package com.cauadev.autenticacaojwt.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cauadev.autenticacaojwt.exceptions.SenhaInvalidaException;
import com.cauadev.autenticacaojwt.model.CredenciaisDTO;
import com.cauadev.autenticacaojwt.model.InfoUsuarioCadastroDTO;
import com.cauadev.autenticacaojwt.model.TokenDTO;
import com.cauadev.autenticacaojwt.model.Usuario;
import com.cauadev.autenticacaojwt.repository.UsuarioRepository;
import com.cauadev.autenticacaojwt.service.JwtService;
import com.cauadev.autenticacaojwt.service.UsuarioServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final UsuarioServiceImpl usuarioService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UsuarioRepository repository;
	
	@PostMapping("/signup")
	@ResponseStatus(value = HttpStatus.CREATED)
	public InfoUsuarioCadastroDTO salvar(@Valid @RequestBody Usuario usuario) {
		Optional<Usuario> vazio = Optional.empty();
		if(repository.findByUsuario(usuario.getUsuario()) != vazio) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,"Usuário já existente.");
		}
		String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		Usuario usuarioSalvo = usuarioService.salvar(usuario);
		return InfoUsuarioCadastroDTO
				.builder()
				.admin(usuarioSalvo.isAdmin())
				.codigo(usuarioSalvo.getId())
				.usuario(usuarioSalvo.getUsuario())
				.build();
	}
	
	@PostMapping("/login")
	public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
		try {
			
			Usuario usuario = Usuario.builder()
			.usuario(credenciais.getUsuario())
			.senha(credenciais.getSenha())
			.build();

			usuarioService.autenticar(usuario);
			String token = jwtService.generateToken(usuario);
			return new TokenDTO(usuario.getUsuario(),token);
		} catch (UsernameNotFoundException | SenhaInvalidaException e ) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
		}
	}

}
