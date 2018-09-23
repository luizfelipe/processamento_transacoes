package com.xpto.dto;

public class TransacoesFinanceiraClassificacaoValidacaoDTO {
	
	private Long semErro;
	private Long regra1;
	private Long regra2;
	private Long regra3;
	private Long regra4;
	private Long total;
	
	public TransacoesFinanceiraClassificacaoValidacaoDTO(Long semErro, Long regra1, Long regra2, Long regra3, Long regra4, Long total) {
		super();
		this.semErro = semErro;
		this.regra1 = regra1;
		this.regra2 = regra2;
		this.regra3 = regra3;
		this.regra4 = regra4;
		this.total = total;
	}
	
	
	public Long getSemErro() {
		return semErro;
	}
	public void setSemErro(Long semErro) {
		this.semErro = semErro;
	}
	public Long getRegra1() {
		return regra1;
	}
	public void setRegra1(Long regra1) {
		this.regra1 = regra1;
	}
	public Long getRegra2() {
		return regra2;
	}
	public void setRegra2(Long regra2) {
		this.regra2 = regra2;
	}
	public Long getRegra3() {
		return regra3;
	}
	public void setRegra3(Long regra3) {
		this.regra3 = regra3;
	}
	public Long getRegra4() {
		return regra4;
	}
	public void setRegra4(Long regra4) {
		this.regra4 = regra4;
	}

	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	
	
	
	
	
}
