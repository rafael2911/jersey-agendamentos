package br.com.cr.rest.jerseyagendamentos.service;

import java.util.List;

import br.com.cr.rest.jerseyagendamentos.model.dao.ItemDao;
import br.com.cr.rest.jerseyagendamentos.model.domain.Item;

public class ItemService {
	
	private ItemDao dao = new ItemDao();
	
	public List<Item> findAll(){
		return dao.findAll();
	}
	
	public Item findById(Long itemId) {
		return dao.findById(itemId);
	}
	
	public Item save(Item item) {
		return dao.save(item);
	}
	
	public Item update(Item item, Long itemId) {
		item.setId(itemId);
		return dao.update(item);
	}
	
	public Item delete(Long itemId) {
		return dao.delete(itemId);
	}
	
}
