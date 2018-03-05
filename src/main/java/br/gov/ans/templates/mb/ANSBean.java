package br.gov.ans.templates.mb;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.util.MessageFactory;


public class ANSBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public void exibirMensagemErroGenerica(){
    	FacesMessage message = MessageFactory.getMessage("erro.generico", FacesMessage.SEVERITY_ERROR, null);
        getFacesContext().addMessage(null, message);
	}
	
	public void exibirMensagemSucessoGenerica(){
		FacesMessage message = MessageFactory.getMessage("sucesso.generico", FacesMessage.SEVERITY_INFO, null);
        getFacesContext().addMessage(null, message);
	}

	public void	exibirMensagemSucesso(String bundleKey){
		FacesMessage message = MessageFactory.getMessage(bundleKey, FacesMessage.SEVERITY_INFO, null);
        getFacesContext().addMessage(null, message);
	}
	
	public void	exibirMensagemErro(String bundleKey){
		FacesMessage message = MessageFactory.getMessage(bundleKey, FacesMessage.SEVERITY_ERROR, null);
        getFacesContext().addMessage(null, message);
	}
	
	public void	exibirMensagemAviso(String bundleKey){
		FacesMessage message = MessageFactory.getMessage(bundleKey, FacesMessage.SEVERITY_WARN, null);
        getFacesContext().addMessage(null, message);
	}
	
	public void	exibirMensagemErroException(Exception ex){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "");
        getFacesContext().addMessage(null, message);
	}
	
	public void exibirMensagemParametrizada(String bundleKey, Severity severity, String messageName, Object[] params){
		FacesMessage message = MessageFactory.getMessage(bundleKey, severity, params);
        FacesContext.getCurrentInstance().addMessage(messageName, message);
	}
		
	protected FacesContext getFacesContext(){
		return FacesContext.getCurrentInstance();
	}
	
	protected ExternalContext getExternalContext(){
		return getFacesContext().getExternalContext();
	}
	
	protected HttpSession getSession(boolean createIfInvalid){
		return (HttpSession) getExternalContext().getSession(createIfInvalid);
	}
	
	protected HttpServletRequest getRequest(){
		return (HttpServletRequest) getExternalContext().getRequest();
	}
	
	protected HttpServletResponse getResponse(){
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	public RequestContext getRequestContext(){
		return RequestContext.getCurrentInstance();
	}
	
	public String getMessageFromBundle(String bundleKey){
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().evaluateExpressionGet(context, "#{msg['"+bundleKey+"']}", String.class);		
	}
	
	public String getUsuarioLogado(){
		return getExternalContext().getUserPrincipal().getName();
	}
}
