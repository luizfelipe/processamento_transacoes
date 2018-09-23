package com.xpto.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.xpto.model.enumerations.ErroValidacaoTransacao;
import com.xpto.model.enumerations.RemetenteTransacao;
import com.xpto.model.enumerations.TipoTransacao;

@Entity
public class TransacaoFinanceira {

	@Id
	private String id;
	
	private Calendar dataHora;
			
	@Enumerated(EnumType.STRING)
	private RemetenteTransacao remetente;

	private String destinatario; 
	
	private Double valor;

	@Enumerated(EnumType.STRING)
	private TipoTransacao tipoTransacao;

	private Boolean valido;
	
	@Enumerated(EnumType.ORDINAL)
	private ErroValidacaoTransacao erroValidacao;
	
	private Integer anoArquivo;
		
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

	public RemetenteTransacao getRemetente() {
		return remetente;
	}
	public void setRemetente(RemetenteTransacao remetente) {
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
	
	public Boolean isValido() {
		return valido;
	}
	public void setValido(Boolean valido) {
		this.valido = valido;
	}
	
	public ErroValidacaoTransacao getErroValidacao() {
		return erroValidacao;
	}
	public void setErroValidacao(ErroValidacaoTransacao erroValidacao) {
		this.erroValidacao = erroValidacao;
	}
	
	public Integer getAnoArquivo() {
		return anoArquivo;
	}
	public void setAnoArquivo(Integer anoArquivo) {
		this.anoArquivo = anoArquivo;
	}
	
	public boolean hasMesmoDiaDoAno(Calendar calendar) {
		return this.getDataHora().get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
				this.getDataHora().get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
	}
	
	public boolean isRemetenteTI() {
		return this.getRemetente().equals(RemetenteTransacao.TI);
	}
	
	public boolean isRemetenteOperacoes() {
		return this.getRemetente().equals(RemetenteTransacao.OPERACOES);
	}
	
	public boolean isRemetenteAdministrativo() {
		return this.getRemetente().equals(RemetenteTransacao.ADMINISTRATIVO);
	}
}
