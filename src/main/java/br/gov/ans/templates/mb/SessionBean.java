package br.gov.ans.templates.mb;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class SessionBean extends ANSBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String usuarioLogado;
		
	public SessionBean(){
		this.usuarioLogado = getExternalContext().getUserPrincipal().getName();
	}
		
	public String getDataAtual(){
		SimpleDateFormat dtHr = new SimpleDateFormat( "EEEE, d' de 'MMMM' de 'yyyy" );
        return dtHr.format( new Date( System.currentTimeMillis() ) );
	}
	
	public String logout() {
		getSession(false).invalidate();
		
		return "/pages/gerenciar_templates.xhtml?faces-redirect=true";
	}

	public String getUsuarioLogado() {
		return usuarioLogado;
	}
	
}
