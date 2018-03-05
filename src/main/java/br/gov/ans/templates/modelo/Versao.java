package br.gov.ans.templates.modelo;

import java.io.Serializable;
import java.util.Date;

public class Versao implements Serializable{

	private String responsavel;
	private Date data;
	private String descricao;

	private String exemplo;
	private byte[] corpo;

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String usuario) {
		this.responsavel = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getExemplo() {
		return exemplo;
	}

	public void setExemplo(String exemplo) {
		this.exemplo = exemplo;
	}

	public byte[] getCorpo() {
		return corpo;
	}

	public void setCorpo(byte[] corpo) {
		this.corpo = corpo;
	}

	public int getHash(){
		return hashCode();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((responsavel == null) ? 0 : responsavel.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Versao other = (Versao) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (responsavel == null) {
			if (other.responsavel != null)
				return false;
		} else if (!responsavel.equals(other.responsavel))
			return false;
		return true;
	}
}
