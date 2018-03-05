package br.gov.ans.templates.clients;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.jboss.logging.Logger;

import br.gov.ans.exceptions.BusinessException;
import br.gov.ans.exceptions.ErrorMessage;
import br.gov.ans.factories.qualifiers.Autenticado;
import br.gov.ans.factories.qualifiers.PropertiesInfo;
import br.gov.ans.factories.qualifiers.Server;
import br.gov.ans.templates.mb.CriacaoTemplate;
import br.gov.ans.templates.modelo.Template;
import br.gov.ans.templates.modelo.Versao;

public class ClientTemplatesBroker implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	@Server
	@ApplicationScoped
	@PropertiesInfo(file="services.properties", key="templates.broker.uri")
	private String uri;
	
	@Inject
	@Autenticado("templates.web")
	private transient Client cliente;
	
	@Inject
	private Logger logger;
	
	public HashMap<String, Object> getTemplates(String filtro, int qtdRegistros, int pagina) throws Exception{
		Response response = cliente.target(uri).path("templates")
			.queryParam("filtro", filtro)
			.queryParam("itens", qtdRegistros)
			.queryParam("pag", pagina)
			.property("Accept", MediaType.APPLICATION_JSON).request().buildGet().invoke();
		
		if(response.getStatusInfo().getFamily() != Family.SUCCESSFUL){
			errorHandling(response);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
				
		String totalRegistros = response.getHeaderString("total_registros");
		map.put("totalRegistros", totalRegistros);
		
		List<Template> templates = response.readEntity(new GenericType<List<Template>>(){});
		map.put("templates", templates);
		
		response.close();
		
		return map;
	}
	
	public String getCorpoTemplate(String identificador) throws Exception{
		Response response = cliente.target(uri).path("templates")
				.path(identificador)
				.path("corpo")
				.request().buildGet().invoke();
		
		if(response.getStatusInfo().getFamily() != Family.SUCCESSFUL){
			errorHandling(response);
		}
		
		String corpo = response.readEntity(String.class);
		
		return corpo;
	}
	
	public void criarTemplate(Template template) throws Exception{
		CriacaoTemplate novoTemplate = new CriacaoTemplate(template);
		
		Response response = cliente.target(uri).path("templates")
				.request().buildPost(Entity.json(novoTemplate)).invoke();
		
		if(response.getStatusInfo().getFamily() != Family.SUCCESSFUL){
			errorHandling(response);
		}
		
		response.close();
	}
	
	public void excluirTemplate(String identificador) throws Exception{
		Response response = cliente.target(uri).path("templates")
				.path(identificador)
				.request().buildDelete().invoke();
		
		if(response.getStatusInfo().getFamily() != Family.SUCCESSFUL){
			errorHandling(response);
		}
		
		response.close();
	}
	
	public void atualizarTemplate(String identificador, Template template) throws Exception{
		CriacaoTemplate novoTemplate = new CriacaoTemplate(template);
		
		Response response = cliente.target(uri).path("templates")
				.path(identificador)
				.request().buildPut(Entity.json(novoTemplate)).invoke();
		
		if(response.getStatusInfo().getFamily() != Family.SUCCESSFUL){
			errorHandling(response);
		}
		
		response.close();
	}
	
	public List<Versao> getVersoes(String identificador) throws Exception{		
		Response response = cliente.target(uri).path("templates")
				.path(identificador)
				.path("versoes")
				.request().buildGet().invoke();
		
		if(response.getStatusInfo().getFamily() != Family.SUCCESSFUL){
			errorHandling(response);
		}
		
		List<Versao> versoes = response.readEntity(new GenericType<List<Versao>>(){});
		
		response.close();
		
		return versoes;
	}
	
	public void errorHandling(Response response) throws Exception{
		if(!response.getHeaderString("Content-Type").equals(MediaType.APPLICATION_JSON)){
			logger.error(response.readEntity(String.class));
			
			response.close();
			throw new Exception("Erro ao utilizar o templates.broker.");
		}
		
		ErrorMessage erro = response.readEntity(ErrorMessage.class);
		response.close();
		
		throw new BusinessException(erro.getError());
	}
}
