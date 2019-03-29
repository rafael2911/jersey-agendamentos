package br.com.cr.rest.jerseyagendamentos.model.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
		name="usuario",
		indexes = { @Index(
				columnList = "username",
				unique = true,
				name = "unique_username")
		}
	)
public class Usuario {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="username", nullable=false, length=60)
	private String username;
	
	@Column(name="password", nullable=false, length=60)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToMany(mappedBy="usuario", fetch=FetchType.EAGER)
	@JsonIgnore
	private List<Agendamento> agendamentos = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public List<Agendamento> getAgendamentos() {
		return agendamentos;
	}
	public void setAgendamentos(List<Agendamento> agendamentos) {
		this.agendamentos = agendamentos;
	}
	
}
