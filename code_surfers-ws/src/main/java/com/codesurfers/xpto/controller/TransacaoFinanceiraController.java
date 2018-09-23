package com.codesurfers.xpto.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesurfers.xpto.dto.TransacoesFinanceirasDatatableDTO;
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
	
	@RequestMapping(value = "/datatablesFormat")
	public TransacoesFinanceirasDatatableDTO buscarValidosDatatablesFormat(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "length", required = false, defaultValue = "25") int length,
			@RequestParam(value = "draw", required = true) int draw) {

		int page = start/length;
		
		Pageable pageableRequest = PageRequest.of(page, length);

		Long quantidadeTransacoesValidas = transacaoFinanceiraRepository.countByValidoTrue();
		
		Page<TransacaoFinanceira> paginaTransacoes = transacaoFinanceiraRepository.findByValidoTrue(pageableRequest);
		
		TransacoesFinanceirasDatatableDTO transacaoDTO = new TransacoesFinanceirasDatatableDTO();
		draw++;
		transacaoDTO.setDraw(draw);
		transacaoDTO.setRecordsTotal(quantidadeTransacoesValidas.intValue());
		transacaoDTO.setRecordsFiltered(quantidadeTransacoesValidas.intValue());
		
		List<TransacaoFinanceira> transacoes = paginaTransacoes.getContent();
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		for (TransacaoFinanceira transacaoFinanceira : transacoes) {
			List<String> valoresAtributos = new ArrayList<String>();
			valoresAtributos.add(transacaoFinanceira.getId());
			valoresAtributos.add(df.format(transacaoFinanceira.getDataHora().getTime()));
			valoresAtributos.add(transacaoFinanceira.getDestinatario());
			valoresAtributos.add(transacaoFinanceira.getValor().toString());
			valoresAtributos.add(transacaoFinanceira.getRemetente().toString());
			valoresAtributos.add(transacaoFinanceira.getTipoTransacao().toString());
			
			transacaoDTO.getData().add(valoresAtributos);
		}

		return transacaoDTO;
	}
	
	@RequestMapping(value = "/mensais")
	public List<Long> buscarTransacoesMensais(
			@RequestParam(value = "ano", required = false, defaultValue = "2018") int ano) {
		List<Tuple> lista = this.transacaoFinanceiraRepository.listTransacoesPorMes(ano);
		List<Long> result = new ArrayList<>();
		for (Tuple tuple : lista) {
			result.add( Long.parseLong( tuple.get("qtd").toString() ));
		}
		return result;

	}
}
