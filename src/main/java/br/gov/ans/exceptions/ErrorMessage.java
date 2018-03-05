package br.gov.ans.exceptions;

import java.io.Serializable;

import javax.ws.rs.core.Response.Status;

public class ErrorMessage implements Serializable{
	
	private String code;
	private String error;
	
	public ErrorMessage(){
		
	}

	public ErrorMessage(String error) {
		this.error = error;
	}
	
	public ErrorMessage(String code, String error) {
		super();
		this.code = code;
		this.error = error;
	}
	
	public ErrorMessage(Status code, String error) {
		super();
		this.code = String.valueOf(code.getStatusCode());
		this.error = error;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
}
