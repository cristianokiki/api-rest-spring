package com.money.api.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Endereco {

	@Size(min = 3, max = 50)
	@Column(name = "logradouro", length = 50)
	private String logradouro;

	@Column(name = "numero")
	private Integer numero;

	@Size(min = 0, max = 100)
	@Column(name = "complemento", length = 100)
	private String complemento;

	@Size(min = 0, max = 50)
	@Column(name = "bairro", length = 50)
	private String bairro;

	@Size(min = 0, max = 8)
	@Column(name = "cep", length = 8)
	private String cep;

	@NotNull
	@Size(min = 0, max = 50)
	@Column(name = "cidade", length = 50, nullable = false)
	private String cidade;

	@Size(min = 0, max = 50)
	@Column(name = "estado", length = 50)
	private String estado;

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
