package com.cauadev.autenticacaojwt.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InfoUsuarioCadastroDTO {

	private Integer codigo;
	private String usuario;
	private boolean admin;
}
