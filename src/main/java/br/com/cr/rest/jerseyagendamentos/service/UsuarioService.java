package br.com.cr.rest.jerseyagendamentos.service;

import java.util.List;

import br.com.cr.rest.jerseyagendamentos.model.dao.UsuarioDao;
import br.com.cr.rest.jerseyagendamentos.model.domain.Usuario;

public class UsuarioService {
	
	private UsuarioDao dao = new UsuarioDao();
	
	public List<Usuario> findAll(){
		return dao.findAll();
	}
	
	public Usuario findById(Long usuarioId) {
		return dao.findById(usuarioId);
	}
	
	public Usuario save(Usuario usuario) {
		return dao.save(usuario);
	}
	
	public Usuario update(Long usuarioId, Usuario usuario) {
		usuario.setId(usuarioId);
		return dao.update(usuario);
	}
	
	public Usuario delete(Long usuarioId) {
		return dao.delete(usuarioId);
	}
	
}
