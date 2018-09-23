package com.xpto.controller;

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

import com.xpto.dto.TransacaoFinanceiraDadosDTO;
import com.xpto.dto.TransacoesFinanceirasDatatableDTO;
import com.xpto.model.TransacaoFinanceira;
import com.xpto.model.enumerations.ErroValidacaoTransacao;
import com.xpto.repository.TransacaoFinanceiraRepository;

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

		Page<TransacaoFinanceira> paginaTransacoes = transacaoFinanceiraRepository.findPaginacao(true,pageableRequest);

		return paginaTransacoes.getContent();
	}
	
	@RequestMapping(value = "/datatablesFormat")
	public TransacoesFinanceirasDatatableDTO buscarValidosDatatablesFormat(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "validas", required = false, defaultValue = "true") boolean validas,
			@RequestParam(value = "length", required = false, defaultValue = "25") int length,
			@RequestParam(value = "draw", required = true) int draw) {

		int page = start/length;
		
		Pageable pageableRequest = PageRequest.of(page, length);
		Long quantidade = 0L;
		if( validas ) {
			quantidade = transacaoFinanceiraRepository.countByValidoTrue();
		}else {
			quantidade = transacaoFinanceiraRepository.countByValidoFalse();
		}
		Page<TransacaoFinanceira> paginaTransacoes = transacaoFinanceiraRepository.findPaginacao(validas,pageableRequest);
		
		TransacoesFinanceirasDatatableDTO transacaoDTO = new TransacoesFinanceirasDatatableDTO();
		draw++;
		transacaoDTO.setDraw(draw);
		transacaoDTO.setRecordsTotal(quantidade.intValue());
		transacaoDTO.setRecordsFiltered(quantidade.intValue());
		
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
			valoresAtributos.add(transacaoFinanceira.getErroValidacao().toString());
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
	
	@RequestMapping(value = "/dados")
	public TransacaoFinanceiraDadosDTO buscarDadosPainel(
			@RequestParam(value = "ano", required = false, defaultValue = "2018") int ano) {
		
		Long semErro = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.SEM_ERRO, ano);
	    Long regra1 = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_001, ano);
		Long regra2 = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_002, ano);
		Long regra3 = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_003, ano);
		Long regra4 = this.transacaoFinanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_004, ano);
		
		TransacaoFinanceiraDadosDTO dados = new TransacaoFinanceiraDadosDTO(semErro, regra1, regra2, regra3, regra4);
		return dados;

	}
}
