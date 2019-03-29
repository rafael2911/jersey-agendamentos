package br.com.cr.rest.jerseyagendamentos.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.cr.rest.jerseyagendamentos.exceptions.DaoException;
import br.com.cr.rest.jerseyagendamentos.exceptions.ErrorCode;
import br.com.cr.rest.jerseyagendamentos.model.domain.Agendamento;
import br.com.cr.rest.jerseyagendamentos.model.domain.Item;
import br.com.cr.rest.jerseyagendamentos.model.domain.Usuario;

public class AgendamentoDao {
	
	public List<Agendamento> findAll(){
		EntityManager em = JpaUtil.getEntityManager();
		List<Agendamento> agendamentos;
		
		try {
			agendamentos = em.createQuery("from Agendamento a join fetch a.usuario join fetch a.item", Agendamento.class).getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao listar agendamentos: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}
		
		return agendamentos;
	}
	
	public Agendamento save(Agendamento agendamento) {
		if(!agendamentoIsValid(agendamento)) {
			throw new DaoException("Agendamento com dados incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		if(agendamento.getItem().getId() <= 0) {
			throw new DaoException("O id do item deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		if(agendamento.getUsuario().getId() <= 0) {
			throw new DaoException("O id do usuario deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		
		try {
			em.getTransaction().begin();
			Usuario usuario = em.find(Usuario.class, agendamento.getUsuario().getId());
			Item item = em.find(Item.class, agendamento.getItem().getId());
			agendamento.setItem(item);
			agendamento.setUsuario(usuario);
			em.persist(agendamento);
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Usuário ou Item não localizados no BD!", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao salvar agendamento no BD: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return agendamento;
	}
	
	private boolean agendamentoIsValid(Agendamento agendamento) {
		try {
			if(agendamento.getItem() == null || agendamento.getUsuario() == null) {
				return false;
			}
			
			if(agendamento.getInicio() == null || agendamento.getFim() == null || agendamento.getMotivo().isEmpty()) {
				return false;
			}
		}catch (NullPointerException ex) {
			return false;
		}
		
		return true;
	}
	
}
