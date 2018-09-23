package com.codesurfers.xpto.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesurfers.xpto.model.TransacaoFinanceira;
import com.codesurfers.xpto.repository.TransacaoFinanceiraRepository;

@RestController
@RequestMapping("/transacoes")
public class TransacaoFinanceiraController {

	private TransacaoFinanceiraRepository transacaoFinanceiraRepository;

	@Autowired
	public TransacaoFinanceiraController(TransacaoFinanceiraRepository transacaoFinanceiraRepository) {
		this.transacaoFinanceiraRepository = transacaoFinanceiraRepository;
	}

	@RequestMapping(value = "/")
	public Collection<TransacaoFinanceira> buscarValidos(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "100") int limit) {

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<TransacaoFinanceira> paginaTransacoes = transacaoFinanceiraRepository.findByValidoTrue(pageableRequest);

		return paginaTransacoes.getContent();

	}
}
