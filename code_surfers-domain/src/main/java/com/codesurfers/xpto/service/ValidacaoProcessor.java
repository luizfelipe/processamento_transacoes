package com.codesurfers.xpto.service;

import com.codesurfers.xpto.model.Transacao;

public interface ValidacaoProcessor {

	Transacao executar(Transacao transacao);
}
