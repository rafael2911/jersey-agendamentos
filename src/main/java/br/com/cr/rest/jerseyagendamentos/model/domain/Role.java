package br.com.cr.rest.jerseyagendamentos.model.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
	ADMINISTRADOR("Administrador"), COORDENADOR("Coordenador"), OPERADOR("Operador");
	
	private String descricao;
	
	private Role(String descricao) {
		this.descricao = descricao;
	}
	
	@JsonValue
	public String getDescricao() {
		return descricao;
	}
}
