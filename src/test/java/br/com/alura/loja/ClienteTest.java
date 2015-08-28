package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import junit.framework.Assert;

public class ClienteTest {
	private HttpServer server;
	private Client client;
	private WebTarget target;
	
	@Before
	public void startaServidor(){
		this.server = Servidor.inicializaServidor();
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		client = ClientBuilder.newClient(config);
		target = client.target("http://localhost:8080");
	}
	
	@After
	public void mataServidor(){
		this.server.stop();
	}
	
	
	@Test
	public void testaQueAConexaoComServidorFunciona() throws Exception {
		WebTarget target = client.target("http://www.mocky.io");
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
	}
	
	@Test
	public void deveSalvarUmNovoCarrinhoNoServidor(){
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
		carrinho.setRua("Rua Vergueiro");
		carrinho.setCidade("São Paulo");
		String xml = carrinho.toXML();
		
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
		Response response = target.path("/carrinhos").request().post(entity);		
		Assert.assertEquals(201, response.getStatus());
		
		String location = response.getHeaderString("Location");		
		String conteudo = client.target(location).request().get(String.class);
		Assert.assertTrue(conteudo.contains("Tablet"));
	}
	
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazCarrinhoEsperado() {
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		Carrinho carrinho = (Carrinho)new XStream().fromXML(conteudo);
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
}
