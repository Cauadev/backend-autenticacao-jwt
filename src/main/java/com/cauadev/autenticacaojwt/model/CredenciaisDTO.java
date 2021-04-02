package com.cauadev.autenticacaojwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredenciaisDTO {
	
	private String usuario;
	private String senha;

}
