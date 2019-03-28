package br.com.cr.rest.jerseyagendamentos.model.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="agendamentos")
public class Agendamento {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="data_hora_inicio", nullable=false)
	@JsonFormat(pattern="dd/MM/yyyy hh:mm")
	private LocalDateTime inicio;
	
	@Column(name="data_hora_fim", nullable=false)
	@JsonFormat(pattern="dd/MM/yyyy hh:mm")
	private LocalDateTime fim;
	
	@Column(nullable=false)
	@Lob
	private String motivo;
	
	@ManyToOne
	@Column(name="usuario_id")
	private Usuario usuario;
	
	@ManyToOne
	@Column(name="item_id")
	private Item item;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public LocalDateTime getFim() {
		return fim;
	}

	public void setFim(LocalDateTime fim) {
		this.fim = fim;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
}
