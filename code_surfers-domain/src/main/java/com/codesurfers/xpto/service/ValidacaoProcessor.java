package com.codesurfers.xpto.service;

import com.codesurfers.xpto.model.TransacaoFinanceira;

public interface ValidacaoProcessor {

	TransacaoFinanceira executar(TransacaoFinanceira transacao);
}
