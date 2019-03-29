package br.com.cr.rest.jerseyagendamentos.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.cr.rest.jerseyagendamentos.model.domain.Agendamento;
import br.com.cr.rest.jerseyagendamentos.service.AgendamentoService;

@Path("agendamentos")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AgendamentoResource {
	
	private AgendamentoService service = new AgendamentoService();
	
	@GET
	public Response getAgendamentos(){
		List<Agendamento> agendamentos = service.findAll();
		return Response.ok().entity(agendamentos).build();
	}
	
	@POST
	@Path("{usuarioId}")
	public Response salvar(@PathParam("usuarioId") Long usuarioId, Agendamento agendamento) {
		service.save(agendamento, usuarioId);
		return Response.ok().entity(agendamento).build();
	}
	
}
