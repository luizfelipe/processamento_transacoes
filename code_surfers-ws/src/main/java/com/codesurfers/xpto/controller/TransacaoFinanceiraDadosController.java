package com.codesurfers.xpto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesurfers.xpto.model.TransacaoFinanceira;
import com.codesurfers.xpto.model.TransacaoFinanceiraDadosResponse;
import com.codesurfers.xpto.model.TransacaoFinanceiraResponse;
import com.codesurfers.xpto.model.enumerations.ErroValidacaoTransacao;
import com.codesurfers.xpto.repository.TransacaoFinanceiraRepository;

@RestController
@RequestMapping("/dados")
public class TransacaoFinanceiraDadosController {
	private TransacaoFinanceiraRepository transacaoFinanceiraRepository;
	
	@Autowired
	public TransacaoFinanceiraDadosController(TransacaoFinanceiraRepository transacaoFinanceiraRepository) {
		this.transacaoFinanceiraRepository = transacaoFinanceiraRepository;
	}
	
	
	@RequestMapping(value = "/")
	public TransacaoFinanceiraDadosResponse buscarValidos(
			@RequestParam(value = "ano", required = false, defaultValue = "2018") int ano) {
		
		Long semErro = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.SEM_ERRO, ano);
	    Long regra1 = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_001, ano);
		Long regra2 = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_002, ano);
		Long regra3 = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_003, ano);
		Long regra4 = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_004, ano);
		
		TransacaoFinanceiraDadosResponse dados = new TransacaoFinanceiraDadosResponse(semErro, regra1, regra2, regra3, regra4);
		return dados;

	}
	
	
}
