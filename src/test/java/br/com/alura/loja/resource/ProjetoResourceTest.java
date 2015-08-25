package br.com.alura.loja.resource;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.Servidor;
import br.com.alura.loja.modelo.Projeto;
import junit.framework.Assert;

public class ProjetoResourceTest {
	
	private HttpServer server;

	@Before
	public void startaServidor(){
		this.server = Servidor.inicializaServidor();
	}
	
	@After
	public void mataServidor() {
		this.server.stop();
	}

	@Test
	public void buscaProjetosApartirDaURI() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		Projeto projeto = (Projeto) new XStream().fromXML(conteudo);
		assertEquals("Minha Loja", projeto.getNome());
	}
	
	@Test
	public void salvaNovoProjetoNoServidor() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		
		Projeto projeto = new Projeto(2L, "Meu novo emprego", 2015);
		String xml = projeto.toXML();
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);		
		Response response = target.path("/projetos").request().post(entity);
		Assert.assertEquals("<status>sucesso</status>", response.readEntity(String.class));
	}

}
