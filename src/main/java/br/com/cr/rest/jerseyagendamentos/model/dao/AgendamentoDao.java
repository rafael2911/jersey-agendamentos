package br.com.cr.rest.jerseyagendamentos.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
	
	public List<Agendamento> findByUsuario(Long usuarioId){
		
		if(usuarioId <= 0) {
			throw new DaoException("O id do usuário deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Agendamento> agendamentos;
		
		try {
			agendamentos = em.createQuery("from Agendamento a "
											+ "join fetch a.usuario "
											+ "join fetch a.item "
											+ "where a.usuario.id = :usuarioId", Agendamento.class)
					.setParameter("usuarioId", usuarioId)
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao listar agendamentos por usuário: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}
		
		if(agendamentos == null) {
			throw new DaoException("Nenhuma agendamento encontrado para o usuário de id " + usuarioId, ErrorCode.NOT_FOUND);
		}
		
		return agendamentos;
		
	}
	
	public List<Agendamento> findByItem(Long itemId){
		
		if(itemId <= 0) {
			throw new DaoException("O id do item deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Agendamento> agendamentos;
		
		try {
			agendamentos = em.createQuery("from Agendamento a "
						+ "join fetch a.usuario "
						+ "join fetch a.item "
						+ "where a.item.id = :itemId", Agendamento.class)
				.setParameter("itemId", itemId)
				.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar agendamentos por item: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(agendamentos == null) {
			throw new DaoException("Nenhum registro encontrado para o item de id " + itemId, ErrorCode.NOT_FOUND);
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
		
		if(agendamento.getInicio().compareTo(agendamento.getFim()) >= 0) {
			throw new DaoException("A data e hora inicial deve ser maior que a final.", ErrorCode.BAD_REQUEST);
		}
		
		if(AgendamentoDuplicado(agendamento)) {
			throw new DaoException("Agendamento para o item " + agendamento.getItem().getId() + " já registrado para período!", ErrorCode.CONFLICT);
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
	
	public Agendamento update(Agendamento agendamento) {
		
		if(agendamento.getId() <= 0) {
			throw new DaoException("O id do agendamento deve ser maior que zero", ErrorCode.BAD_REQUEST);
		}
		
		if(!agendamentoIsValid(agendamento)) {
			throw new DaoException("Agendamento com dados incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		if(agendamento.getItem().getId() <= 0 || agendamento.getUsuario().getId() <= 0) {
			throw new DaoException("O id do usuário e do item devem ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		if(agendamento.getInicio().compareTo(agendamento.getFim()) >= 0) {
			throw new DaoException("A data e hora inicial deve ser maior que a final.", ErrorCode.BAD_REQUEST);
		}
		
		if(AgendamentoDuplicado(agendamento)) {
			throw new DaoException("Agendamento para o item " + agendamento.getItem().getId() + " já registrado para período!", ErrorCode.CONFLICT);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Agendamento agendamentoManaged;
		
		try {
			em.getTransaction().begin();

			agendamento.setItem(em.find(Item.class, agendamento.getItem().getId()));
			agendamento.setUsuario(em.find(Usuario.class,  agendamento.getUsuario().getId()));
			
			agendamentoManaged = em.find(Agendamento.class, agendamento.getId());
			
			if(agendamentoManaged.getUsuario().getId() != agendamento.getUsuario().getId()) {
				throw new NotAuthorizedException("O usuário informado na URL não é o proprietário do agendamento!", Response.status(Status.UNAUTHORIZED));
			}
			
			
			
			agendamentoManaged.setInicio(agendamento.getInicio());
			agendamentoManaged.setFim(agendamento.getFim());
			agendamentoManaged.setMotivo(agendamento.getMotivo());
			agendamentoManaged.setItem(agendamento.getItem());
			agendamentoManaged.setUsuario(agendamento.getUsuario());
			
			
			
			em.getTransaction().commit();
			
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("O usuário e o item não podem ser nulos!", ErrorCode.NOT_FOUND);
		}catch (NotAuthorizedException ex) {
			em.getTransaction().rollback();
			throw new DaoException(ex.getMessage(), ErrorCode.UNAUTHORIZED);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao atualizar agendamento: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return agendamentoManaged;
		
	}
	
	public Agendamento delete(Long agendamentoId, Long usuarioId) {
		
		if(agendamentoId<= 0 || usuarioId <= 0) {
			throw new DaoException("O id do agendamentodeve e do usuário deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Agendamento agendamento;
		
		try {
			em.getTransaction().begin();
			agendamento = em.find(Agendamento.class, agendamentoId);
			
			if(agendamento.getUsuario().getId() != usuarioId) {
				throw new NotAuthorizedException("O usuário informado na URL não é o proprietário do agendamento!", Response.status(Status.UNAUTHORIZED));
			}
			
			em.remove(agendamento);
			
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Agendamento de id " + agendamentoId + " não localizado!", ErrorCode.NOT_FOUND);
		}catch (NotAuthorizedException ex) {
			em.getTransaction().rollback();
			throw new DaoException(ex.getMessage(), ErrorCode.UNAUTHORIZED);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao apagar agendamento: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return agendamento;
		
	}
	
	private boolean AgendamentoDuplicado(Agendamento agendamento) {
		EntityManager em = JpaUtil.getEntityManager();
		List<Agendamento> agendamentoResult;
		
		try {
			agendamentoResult = em.createQuery("from Agendamento a "
					+ "join fetch a.usuario "
					+ "join fetch a.item "
					+ "where a.item.id = :itemId "
						+ "and (:inicio between a.inicio and a.fim or :fim between a.inicio and a.fim) "
						+ "and a.id != :agendamentoId", Agendamento.class)
			.setParameter("itemId", agendamento.getItem().getId())
			.setParameter("inicio", agendamento.getInicio())
			.setParameter("fim", agendamento.getFim())
			.setParameter("agendamentoId", (agendamento.getId() == null) ? 0L : agendamento.getId())
			.getResultList();
			
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar produto duplicado: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(agendamentoResult.isEmpty())
			return false;
		
		return true;
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
