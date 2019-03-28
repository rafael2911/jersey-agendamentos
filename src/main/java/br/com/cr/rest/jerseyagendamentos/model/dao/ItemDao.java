package br.com.cr.rest.jerseyagendamentos.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.cr.rest.jerseyagendamentos.exceptions.DaoException;
import br.com.cr.rest.jerseyagendamentos.exceptions.ErrorCode;
import br.com.cr.rest.jerseyagendamentos.model.domain.Item;

public class ItemDao {
	
	public List<Item> findAll(){
		EntityManager em = JpaUtil.getEntityManager();
		List<Item> itens;
		
		try {
			itens = em.createQuery("from Item i", Item.class).getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao listar itens: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return itens;
	}
	
	public Item findById(Long itemId) {
		if(itemId <= 0) {
			throw new DaoException("O Id deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Item item;
		
		try {
			item = em.find(Item.class, itemId);
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar item por id: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(item == null) {
			throw new DaoException("Item com id " + itemId + " não localizado!", ErrorCode.NOT_FOUND);
		}
		
		return item;
	}
	
	public Item save(Item item) {
		EntityManager em = JpaUtil.getEntityManager();
		
		if(!itemIsValid(item)){
			throw new DaoException("Item com dados incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		try {
			em.getTransaction().begin();
			em.persist(item);
			em.getTransaction().commit();
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao salvar item no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return item;
	}
	
	public Item update(Item item) {
		EntityManager em = JpaUtil.getEntityManager();
		Item itemManaged;
		
		if(item.getId() <= 0) {
			throw new DaoException("O id do item deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		if(!itemIsValid(item)) {
			throw new DaoException("Item com dados incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		try {
			em.getTransaction().begin();
			itemManaged = em.find(Item.class, item.getId());
			itemManaged.setDescricao(item.getDescricao());
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Item informado para atualização não localizado!", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao atualizar item: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return itemManaged;
	}
	
	public Item delete(Long itemId) {
		EntityManager em = JpaUtil.getEntityManager();
		Item item;
		
		if(itemId <= 0) {
			throw new DaoException("O id do item deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		try {
			em.getTransaction().begin();
			item = em.find(Item.class, itemId);
			em.remove(item);
			em.getTransaction().commit();
		}catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Item informado para exclusão não localizado!", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao excluir item: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return item;
	}
	
	private boolean itemIsValid(Item item) {
		try {
			if(item.getDescricao().isEmpty()) {
				return false;
			}
		}catch (NullPointerException ex) {
			throw new DaoException("Item com dados incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		return true;
	}
}
