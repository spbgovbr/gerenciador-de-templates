<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="pt-BR">
    <head>
        <meta name="description" content="ANS">
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <meta http-equiv="pragma" content="no-cache">
        <meta name="keywords" content="ANS, Agência Nacional de Saúde Suplementar" lang="pt-BR">
        <meta http-equiv="content-language" content="pt-BR">

        <title>ANS - Agência Nacional de Saúde Suplementar</title>

		<link type="text/css" rel="stylesheet" href="/ans-idp/javax.faces.resource/theme.css.xhtml?ln=primefaces-aristo">
		<link type="text/css" rel="stylesheet" href="/ans-idp/javax.faces.resource/primefaces.css.xhtml?ln=primefaces&amp;v=5.2">				
		<link type="text/css" rel="stylesheet" href="https://www.ans.gov.br/templates/templateans/css/bootstrap.min.css" />
		<link type="text/css" rel="stylesheet" href="https://www.ans.gov.br/templates/templateans/css/style.css" />
		<link type="text/css" rel="stylesheet" href="https://www.ans.gov.br/images/PrimeFaces/css/style-prime-custom-ans.css" />		
		<link rel="icon" href="https://www.ans.gov.br/images/marcas/favicon.ico" type="image/x-icon" />
	
    </head>
	
    <body onload="document.form.j_username.focus()">    
        <div class="topoAreaRestritaOperadoras-ans" id="top-box">
        <div style="margin-top:27px">
		<div class="marca-po-ans">
			<img src="https://www.ans.gov.br/images/marcas/logo-ans-sem-slogan.png" width="230" height="46" alt="ANS - Agência Nacional de Saúde Suplementar" />
		</div>
		<div class="left-po-ans titulo-principal-po-ans">
				Administração de Templates
		</div>
        </div>
        </div>
         
         <div class="clear-po-ans">
			<br />
			<hr style="border-bottom:none; border-top:1px solid #097189;" class="ui-separator ui-state-default ui-corner-all" />
		</div>
       
       <div class="topoAreaRestritaOperadoras-ans">
       
        <div id="element-box" class="login">
	        
	       <div class="centralizaLogin-ans" style="position:absolute; width:300px; margin-left:-150px; left:50%; height: 350px;">
	               <h1>Login</h1>
	               <input type="hidden" value="0.1.2" />
	               <p>Informe seu usuário e senha para acessar a aplicação.</p>              
	               
	               <div class="alert alert-alerta" id="system-message" style="display:<%= (session.getAttribute("ans.login.erro")!=null) ? "block" : "none" %>">
	               <p><b>Erro</b></p>
	               <p><%= session.getAttribute("ans.login.erro") %></p>
	               </div>
	               
	               <% session.setAttribute("ans.login.erro", null); %>
	               
	               <br />
	               <div id="section-box" class="boxcinza-ans">
	                 <form name="form" method="POST" action="<%= response.encodeURL("j_security_check") %>" id="form-login">

						<div class="form-group">
							<label id="mod-login-username-lbl" for="mod-login-username">Usuário</label>
							<input name="j_username" id="mod-login-username" type="text"
								class="form-control campoTexto ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all"
								size="20">
						</div>
						<div class="form-group">
							<label id="mod-login-password-lbl" for="mod-login-password">Senha</label>
							<input name="j_password" id="mod-login-password" type="password"
								class="form-control campoTexto" size="20">
						</div>

						<input type="submit" value="Acessar" id="botao"
							class="btn btn-primary btn-block" />

						</form>
	               </div>
	           </div>
	       <div id="lock"></div>
	    </div>
    </body>
</html>                            
