package br.com.cr.rest.jerseyagendamentos.service;

import java.util.List;

import br.com.cr.rest.jerseyagendamentos.model.dao.AgendamentoDao;
import br.com.cr.rest.jerseyagendamentos.model.domain.Agendamento;
import br.com.cr.rest.jerseyagendamentos.model.domain.Usuario;

public class AgendamentoService {
	
	private AgendamentoDao dao = new AgendamentoDao();
	
	public List<Agendamento> findAll(){
		return dao.findAll();
	}
	
	public List<Agendamento> findByUsuario(Long usuarioId){
		return dao.findByUsuario(usuarioId);
	}

	public Agendamento save(Agendamento agendamento, Long usuarioId) {
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		agendamento.setUsuario(usuario);
		return dao.save(agendamento);
	}
	
	public Agendamento update(Agendamento agendamento, Long usuarioId) {
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		agendamento.setUsuario(usuario);
		return dao.update(agendamento);
	}
	
}
