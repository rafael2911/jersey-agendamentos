package br.com.cr.rest.jerseyagendamentos.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.cr.rest.jerseyagendamentos.exceptions.DaoException;
import br.com.cr.rest.jerseyagendamentos.exceptions.ErrorCode;
import br.com.cr.rest.jerseyagendamentos.model.domain.Usuario;

public class UsuarioDao {
	
	public List<Usuario> findAll(){
		EntityManager em = JpaUtil.getEntityManager();
		List<Usuario> usuarios;
		
		try {
			usuarios = em.createQuery("from Usuario u", Usuario.class).getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao listar usuários", ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return usuarios;
	}
	
	public Usuario findById(Long usuarioId) {
		if(usuarioId <= 0) {
			throw new DaoException("O id do usuario deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Usuario usuario;
		
		try {
			usuario = em.find(Usuario.class, usuarioId);
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar usuario por id" + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(usuario == null) {
			throw new DaoException("Usuario de id " + usuarioId + " não localizado.", ErrorCode.NOT_FOUND);
		}
		
		return usuario;
	}
	
	public Usuario save(Usuario usuario) {
		
		if(!usuarioIsValid(usuario)) {
			throw new DaoException("Dados do usuário incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		
		try {
			em.getTransaction().begin();
			em.persist(usuario);
			em.getTransaction().commit();
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao salvar usuário: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return usuario;
	}
	
	public Usuario update(Usuario usuario) {
		
		if(usuario.getId() <= 0) {
			throw new DaoException("O id do usuário deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		if(!usuarioIsValid(usuario)) {
			throw new DaoException("Usuário com dados incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Usuario usuarioManaged;
		
		try {
			em.getTransaction().begin();
			usuarioManaged = em.find(Usuario.class, usuario.getId());
			usuarioManaged.setUsername(usuario.getUsername());
			usuarioManaged.setPassword(usuario.getPassword());
			usuarioManaged.setRole(usuario.getRole());
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Usuário de id " + usuario.getId() + " não localizado no BD", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao atualzar usuário: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return usuarioManaged;
	}
	
	public Usuario delete(Long usuarioId) {
		
		if(usuarioId <= 0) {
			throw new DaoException("O id do usuário deve ser maior do que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Usuario usuario;
		
		try {
			em.getTransaction().begin();
			usuario = em.find(Usuario.class, usuarioId);
			em.remove(usuario);
			em.getTransaction().commit();
		}catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Usuário de id " + usuarioId + " não localizado no BD.", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao remover usuário: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return usuario;
		
	}
	
	private boolean usuarioIsValid(Usuario usuario) {
		try {
			if(usuario.getUsername().isEmpty() || usuario.getPassword().isEmpty() || usuario.getRole() == null) {
				return false;
			}
		}catch (NullPointerException ex) {
			throw new DaoException("Dados do usuario incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		return true;
	}
	
}
