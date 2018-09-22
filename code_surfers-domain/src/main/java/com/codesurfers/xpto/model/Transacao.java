package com.codesurfers.xpto.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Transacao {

	@Id
	private String id;
	
	private Calendar dataHora;
	
	private Remetente remetente;

	private String destinatario; 
	
	private Double valor;

	private TipoTransacao tipoTransacao;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Calendar getDataHora() {
		return dataHora;
	}
	public void setDataHora(Calendar dataHora) {
		this.dataHora = dataHora;
	}

	public Remetente getRemetente() {
		return remetente;
	}
	public void setRemetente(Remetente remetente) {
		this.remetente = remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}

	public TipoTransacao getTipoTransacao() {
		return tipoTransacao;
	}
	public void setTipoTransacao(TipoTransacao tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}
}
