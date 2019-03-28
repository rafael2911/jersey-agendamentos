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

import br.com.cr.rest.jerseyagendamentos.model.domain.Item;
import br.com.cr.rest.jerseyagendamentos.service.ItemService;

@Path("/itens")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ItemResource {
	
	private ItemService service = new ItemService();
	
	@GET
	public Response getItens() {
		List<Item> itens = service.findAll();
		
		return Response.ok().entity(itens).build();
	}
	
	@GET
	@Path("/{itemId}")
	public Response getItem(@PathParam("itemId") Long itemId) {
		Item item = service.findById(itemId);
		return Response.ok().entity(item).build();
	}
	
	@POST
	public Response salvar(Item item) {
		service.save(item);
		return Response.status(Status.CREATED).entity(item).build();
	}
	
	@PUT
	@Path("/{itemId}")
	public Response atualizar(@PathParam("itemId") Long itemId, Item item) {
		service.update(item, itemId);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{itemId}")
	public Response apagar(@PathParam("itemId") Long itemId) {
		service.delete(itemId);
		return Response.noContent().build();
	}
	
}
