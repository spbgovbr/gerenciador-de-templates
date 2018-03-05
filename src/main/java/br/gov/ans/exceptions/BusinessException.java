package br.gov.ans.exceptions;

import java.io.Serializable;

public class BusinessException extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;

	private String message;

	public BusinessException(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}	
}
