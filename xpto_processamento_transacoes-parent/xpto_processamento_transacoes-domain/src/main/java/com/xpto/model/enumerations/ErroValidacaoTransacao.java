package com.xpto.model.enumerations;

public enum ErroValidacaoTransacao {

	SEM_ERRO(0, "Entrada válida"),
	ERRO_001(1, "Pagamento com remetente diferente de FINANCEIRO"),
	ERRO_002(3, "Pagamento sem fatura válida"),
	ERRO_003(3, "Retirada com remetente diferente de COMERCIAL"),
	ERRO_004(4, "Depósito não é autoriazado para nenhum remetente");
	
	private int identificador;
	private String descricao;
	
	private ErroValidacaoTransacao(int identificador, String descricao) {
		this.identificador = identificador;
		this.descricao = descricao;
	}
	
	public int getIdentificador() {
		return identificador;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
