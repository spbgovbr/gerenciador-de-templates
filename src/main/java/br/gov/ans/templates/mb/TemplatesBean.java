package br.gov.ans.templates.mb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.gov.ans.exceptions.BusinessException;
import br.gov.ans.templates.clients.ClientTemplatesBroker;
import br.gov.ans.templates.modelo.Template;
import br.gov.ans.templates.modelo.Versao;

@Named
@Stateful
@ViewScoped
public class TemplatesBean extends ANSBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Logger logger;
	
	@Inject
	private transient ClientTemplatesBroker templatesBroker;

	DateFormat formatter = new SimpleDateFormat("ddMMyyyyhhmm");
	
	private String filtro;
	private String identificadorTemplateEditado;
	private String historicoSelecionado;

	private List<Versao> versoes;
	private List<Template> templates;
	private LazyDataModel<Template> model;
	
	private Template template;
	
	private int qtdRegistros;

	private UploadedFile arquivoTemplate;
	
	@PostConstruct
	public void init(){
		model = new LazyDataModel<Template>() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public List<Template> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				int numeroPagina = (first / pageSize) + 1;

				try {
					HashMap<String, Object> map = templatesBroker.getTemplates(filtro, pageSize, numeroPagina);
										
					templates = (List<Template>) map.get("templates");
					qtdRegistros = Integer.parseInt(map.get("totalRegistros").toString());

					setRowCount(qtdRegistros);
				} catch (Exception ex) {
					templates = null;
					logger.error(ex, ex);
					exibirMensagemErro(ex.getMessage());
				}

				return templates;
			}
		};
		
		novoTemplate();
	}
	
	public void novoTemplate(){
		template = new Template();
		template.setResponsavel(getUsuarioLogado());
	}
	
	public void editarTemplate(Template template){
		this.template = template;
		this.template.setResponsavel(getUsuarioLogado());
		
		this.identificadorTemplateEditado =  template.getNome();
	}
	
	public void salvar(){
		try {
			validarTemplate(template);
			
			template.setCorpo(getCorpoArquivo());
			
			templatesBroker.criarTemplate(template);
			
			getRequestContext().execute("PF('templateDialog').hide();");
			
			exibirMensagemSucesso("sucesso.template.criado");
			novoTemplate();
		}catch(IOException ex){
			logger.error(ex, ex);
			exibirMensagemErro("erro.carregar.arquivo.template");
		}catch (Exception ex) {
			logger.error(ex, ex);
			exibirMensagemErroException(ex);
		}		
	}
	
	public void getHistoricoTemplate(String identificador){
		this.historicoSelecionado = identificador;
		
		try {
			this.versoes = templatesBroker.getVersoes(identificador);
		} catch (Exception ex) {
			logger.error(ex, ex);
			exibirMensagemErroException(ex);
		}
	}
	
	public void atualizarTemplate(){
		try {
			validarTemplate(template);
			
			if(arquivoTemplate != null && StringUtils.isNotBlank(arquivoTemplate.getFileName())){
				template.setCorpo(getCorpoArquivo());
			}else{
				exibirMensagemAviso("aviso.arquivo.template.nao.anexado");
			}
			
			templatesBroker.atualizarTemplate(identificadorTemplateEditado, template);
			
			getRequestContext().execute("PF('editTemplateDialog').hide();");
			
			exibirMensagemSucesso("sucesso.template.atualizado");
		} catch (Exception ex) {
			logger.error(ex, ex);
			exibirMensagemErroException(ex);
		}
	}
	
	public void atualizarCorpoTemplate(){
		try {
			templatesBroker.atualizarTemplate(template.getNome(), template);
			
			getRequestContext().execute("PF('editCorpoDialog').hide();");
			
			exibirMensagemSucesso("sucesso.template.atualizado");
		} catch (Exception ex) {
			logger.error(ex, ex);
			exibirMensagemErroException(ex);
		}
	}
	
	public void validarTemplate(Template template) throws BusinessException{
		if(StringUtils.isBlank(template.getNome())){
			throw new BusinessException(getMessageFromBundle("erro.identificador.obrigatorio"));
		}
		
		if(StringUtils.isBlank(template.getDescricao())){
			throw new BusinessException(getMessageFromBundle("erro.descricao.obrigatoria"));
		}
		
		if(StringUtils.isBlank(template.getExemplo())){
			throw new BusinessException(getMessageFromBundle("erro.exemplo.obrigatorio"));
		}
	}
	
	public String getCorpoArquivo() throws IOException, BusinessException{
		if(arquivoTemplate == null){
			throw new BusinessException(getMessageFromBundle("erro.arquivo.obrigatorio"));
		}
		
		if(arquivoTemplate.getSize() < 1L){
			throw new BusinessException(getMessageFromBundle("erro.arquivo.template.vazio"));
		}
		
		return IOUtils.toString(arquivoTemplate.getInputstream());
	}
	
	public StreamedContent downloadTemplate(String identificador){
		try{
			String corpoTemplate = templatesBroker.getCorpoTemplate(identificador);			
			
			InputStream inputStram = new ByteArrayInputStream(corpoTemplate.getBytes());
			
			return new DefaultStreamedContent(inputStram, "application/octet-stream", identificador +".mustache");
		}catch (Exception ex) {
			logger.error(ex, ex);
			exibirMensagemErroException(ex);
			return null;
		}
	}

	public void excluirTemplate(String identificador){
		try {
			templatesBroker.excluirTemplate(identificador);
			exibirMensagemSucesso("sucesso.template.excluido");
		} catch (Exception ex) {
			logger.error(ex, ex);
			exibirMensagemErroException(ex);
		}
	}

	public StreamedContent downloadVersao(Versao versao){
		try{
			InputStream inputStram = new ByteArrayInputStream(versao.getCorpo());
			
			return new DefaultStreamedContent(inputStram, "application/octet-stream", historicoSelecionado+"_"+formatter.format(versao.getData()) +".mustache");
		}catch (Exception ex) {
			logger.error(ex, ex);
			exibirMensagemErroException(ex);
			return null;
		}
	}
	
	public void limparListaVersoes(){
		this.historicoSelecionado = null;
		this.versoes = null;
	}
	
	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Template> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Template> model) {
		this.model = model;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public UploadedFile getArquivoTemplate() {
		return arquivoTemplate;
	}

	public void setArquivoTemplate(UploadedFile arquivoTemplate) {
		this.arquivoTemplate = arquivoTemplate;
	}

	public String getIdentificadorTemplateEditado() {
		return identificadorTemplateEditado;
	}

	public void setIdentificadorTemplateEditado(String identificadorTemplateEditado) {
		this.identificadorTemplateEditado = identificadorTemplateEditado;
	}

	public List<Versao> getVersoes() {
		return versoes;
	}

	public void setVersoes(List<Versao> versoes) {
		this.versoes = versoes;
	}

	public String getHistoricoSelecionado() {
		return historicoSelecionado;
	}

}
