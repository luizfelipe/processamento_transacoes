package com.codesurfers.xpto.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codesurfers.xpto.model.TransacaoFinanceira;
import com.codesurfers.xpto.model.enumerations.ErroValidacaoTransacao;
import com.codesurfers.xpto.model.enumerations.TipoTransacao;
import com.codesurfers.xpto.repository.TransacaoFinanceiraRepository;

@Service
public class ValidacaoAssociacaoPagamentoFaturaService {

	private TransacaoFinanceiraRepository transacaoFinanceiraRepository;
	
	@Autowired
	public ValidacaoAssociacaoPagamentoFaturaService(TransacaoFinanceiraRepository transacaoFinanceiraRepository) {
		this.transacaoFinanceiraRepository = transacaoFinanceiraRepository;	
	}
	
	public int atualizarPagamentosSemFaturaValida(int ano) {
		
		/*List<TransacaoFinanceira> pagamentos = transacaoFinanceiraRepository.findByTipoTransacaoAndAnoArquivo(TipoTransacao.PAGAMENTO, ano);
		List<TransacaoFinanceira> faturas = transacaoFinanceiraRepository.findByTipoTransacaoAndAnoArquivo(TipoTransacao.FATURA, ano);
		
		faturas.forEach(f -> f.getDataHora().add(Calendar.DAY_OF_YEAR, 1));
		
		int numeroRegistrosAlterados = 0;
		
		for (TransacaoFinanceira p : pagamentos) {
			long numeroFaturasCorrespondentes = faturas.stream()
					.filter(
						f -> f.getDestinatario().equals(p.getDestinatario()) 
						&& f.getValor().equals(p.getValor())
						&& f.hasMesmoDiaDoAno(p.getDataHora())
						&& (f.isRemetenteTI() || f.isRemetenteOperacoes() || f.isRemetenteAdministrativo()) 
					)
					.count();
			
			if (numeroFaturasCorrespondentes == 0) {
				p.setValido(false);
				p.setErroValidacao(ErroValidacaoTransacao.ERRO_002);
				transacaoFinanceiraRepository.save(p);
				numeroRegistrosAlterados++;
			}
		}*/
		
		return 0;
	}
}
