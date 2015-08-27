package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;

@Path("carrinhos")
public class CarrinhoResource {
	
	/**
	 * Este método recebe na URL um parametro id, enviado pelo request do tipo GET, a anotação @QueryParam indicado que é o parametro
	 * o seu argumento indicado o nome deste parametro na url que foi chamada.
	 * A URL ficaria a seguinte /carrinhos?id=<valor>
	 * @param id
	 * @return
	 */
	/*@GET
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@QueryParam("id") long id){
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toXML();
	}*/
	
	/**
	 * Este metodo diferente do anterior significa que ao invés de gerar uma url que recebe um parametro via GET, deverá gerar
	 * uma URL em si que no meio da url tenha o id, por exemplo /carrinhos/1
	 * Lembre-se de trocar a anotação @QueryParam para @PathParam. e adicionar a anotação @Path no metodo
	 * a anotação @Path recebe como argumento o que deve ser gerado na URL em nosso caso o número do ID do carrinho
	 * 
	 * @param id
	 * @return
	 */
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@PathParam("id") long id){
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toXML();
	}
	
	/* CODIGO FUNCIONA APENAS PARA SABER GERAR JSON
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String busca(@PathParam("id") long id){
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toJson();
	}*/
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response adiciona(String conteudo) {
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		new CarrinhoDAO().adiciona(carrinho);
		URI uri = URI.create("/carrinhos/"+carrinho.getId());
		return Response.created(uri).build();
	}
	
}
