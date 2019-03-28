package br.com.cr.rest.jerseyagendamentos.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.cr.rest.jerseyagendamentos.model.domain.Usuario;
import br.com.cr.rest.jerseyagendamentos.service.UsuarioService;

@Path("/usuarios")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UsuarioResource {
	
	private UsuarioService service = new UsuarioService();
	
	@GET
	public Response getUsuarios(){
		List<Usuario> usuarios = service.findAll();
		return Response.ok()
				.entity(usuarios)
				.build();
		
	}
	
	@GET
	@Path("/{usuarioId}")
	public Response getUsuario(@PathParam("usuarioId") Long usuarioId) {
		Usuario usuario = service.findById(usuarioId);
		return Response.ok()
				.entity(usuario)
				.build();
	}
	
	@POST
	public Response salvar(Usuario usuario) {
		service.save(usuario);
		return Response.status(Status.CREATED)
				.entity(usuario)
				.build();
	}
	
	@PUT
	@Path("/{usuarioId}")
	public Response atualizar(@PathParam("usuarioId") Long usuarioId, Usuario usuario) {
		service.update(usuarioId, usuario);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{usuarioId}")
	public Response apagar(@PathParam("usuarioId") Long usuarioId) {
		service.delete(usuarioId);
		return Response.noContent().build();
	}
	
}
