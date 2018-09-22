package com.codesurfers.xpto.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@RequestMapping("/")
	public Collection<TransacaoFinanceira> buscarValidos() {
		
		return transacaoFinanceiraRepository.findByValidoTrue();
	}
}
