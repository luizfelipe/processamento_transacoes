package com.codesurfers.xpto.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesurfers.xpto.model.TransacaoFinanceira;
import com.codesurfers.xpto.model.enumerations.ErroValidacaoTransacao;
import com.codesurfers.xpto.model.enumerations.TipoTransacao;

public interface TransacaoFinanceiraRepository extends JpaRepository<TransacaoFinanceira, String> {

	List<TransacaoFinanceira> findByValidoTrue();
	
	//TODO: Temporário
	List<TransacaoFinanceira> findTop5000ByValidoTrue();
	
	List<TransacaoFinanceira> findByTipoTransacaoAndAnoArquivo(TipoTransacao tipoTransacao, int ano);
	
	Long countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao erro, int ano);		
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE TransacaoFinanceira t SET t.valido = false, t.erroValidacao = 1 WHERE t.id IN "
			+ "(SELECT tf.id "
			+ "		FROM TransacaoFinanceira tf "
			+ "		WHERE "
			+ "			tf.tipoTransacao = com.codesurfers.xpto.model.enumerations.TipoTransacao.PAGAMENTO "
			+ "			AND tf.remetente != com.codesurfers.xpto.model.enumerations.RemetenteTransacao.FINANCEIRO "
			+ "			AND tf.anoArquivo=:anoArquivo)")
	int updatePagamentosRemetentesInvalidos(@Param("anoArquivo") int anoArquivo);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE TransacaoFinanceira t SET t.valido = false, t.erroValidacao = 3 WHERE t.id IN "
			+ "(SELECT tf.id "
			+ "		FROM TransacaoFinanceira tf "
			+ "		WHERE "
			+ "			tf.tipoTransacao = com.codesurfers.xpto.model.enumerations.TipoTransacao.RETIRADA "
			+ "			AND (tf.remetente != com.codesurfers.xpto.model.enumerations.RemetenteTransacao.COMERCIAL OR tf.valor > 10000) "
			+ "			AND tf.anoArquivo=:anoArquivo)")
	int updateRetiradasInvalidas(@Param("anoArquivo") int anoArquivo);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE TransacaoFinanceira t SET t.valido = false, t.erroValidacao = 4 WHERE t.id IN "
			+ "(SELECT tf.id "
			+ "		FROM TransacaoFinanceira tf "
			+ "		WHERE "
			+ "			tf.tipoTransacao = com.codesurfers.xpto.model.enumerations.TipoTransacao.DEPOSITO "
			+ "			AND tf.anoArquivo=:anoArquivo)")
	int updateDepositosInvalidos(@Param("anoArquivo") int anoArquivo);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="UPDATE transacao_financeira trans " + 
			"   SET erro_validacao = 2, valido=false  " + 
			"   WHERE trans.id in ( " + 
			"	   SELECT INVALIDOS.id FROM (" + 
			"			SELECT TF1.ID,  to_char(TF1.data_hora, 'YYYY-MM-DD') AS data_hora, TF1.destinatario, TF1.valor " + 
			"				FROM public.transacao_financeira TF1  " + 
			"				WHERE TF1.tipo_transacao = 'PAGAMENTO'" + 
			"				 AND TF1.ano_arquivo = 2018" + 
			"			EXCEPT " + 
			"			SELECT PAGAMENTOS.* FROM  " + 
			"			 (SELECT TF1.ID,  to_char(TF1.data_hora, 'YYYY-MM-DD') AS data_hora, TF1.destinatario, TF1.valor " + 
			"				FROM public.transacao_financeira TF1  " + 
			"				WHERE TF1.tipo_transacao = 'PAGAMENTO'" + 
			"				   AND TF1.ano_arquivo = 2018" + 
			"			 ) PAGAMENTOS" + 
			"			INNER JOIN " + 
			"			  (SELECT to_char((cast(cast(TF2.data_hora as date) + 1 as timestamp)), 'YYYY-MM-DD')AS data_hora, TF2.destinatario, TF2.valor " + 
			"				FROM public.transacao_financeira TF2" + 
			"				WHERE TF2.tipo_transacao = 'FATURA' " + 
			"				AND TF2.remetente in ('OPERACOES', 'TI', 'ADMINISTRATIVO')" + 
			"				AND TF2.ano_arquivo = 2018" + 
			"				) FATURAS " + 
			"				ON FATURAS.valor = PAGAMENTOS.valor " + 
			"				and FATURAS.data_hora = PAGAMENTOS.data_hora " + 
			"				and FATURAS.destinatario = PAGAMENTOS.destinatario" + 
			"			) INVALIDOS" + 
			"       )", nativeQuery = true)
	int updatePagamentosInvalidos(@Param("anoArquivo") int anoArquivo);
	
	
	
}
