package com.codesurfers.xpto.model.enumerations;

public enum ErroValidacaoTransacao {

	SEM_ERRO(0),
	ERRO_001(1),
	ERRO_002(3),
	ERRO_003(3),
	ERRO_004(4);
	
	private int identificador;
	
	private ErroValidacaoTransacao(int identificador) {
		this.identificador = identificador;
	}
	
	public int getIdentificador() {
		return identificador;
	}
}
