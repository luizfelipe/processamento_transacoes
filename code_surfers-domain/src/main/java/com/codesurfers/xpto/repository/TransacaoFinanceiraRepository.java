package com.codesurfers.xpto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesurfers.xpto.model.TransacaoFinanceira;

public interface TransacaoFinanceiraRepository extends JpaRepository<TransacaoFinanceira, String> {

	List<TransacaoFinanceira> findByValidoTrue();
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE TransacaoFinanceira t SET t.valido = false, t.erroValidacao = 1 WHERE t.id IN "
			+ "(SELECT tf.id "
			+ "		FROM TransacaoFinanceira tf "
			+ "		WHERE "
			+ "			tf.tipoTransacao = com.codesurfers.xpto.model.enumerations.TipoTransacao.PAGAMENTO "
			+ "			AND tf.remetente != com.codesurfers.xpto.model.enumerations.RemetenteTransacao.FINANCEIRO "
			+ "			AND tf.anoArquivo=:anoArquivo)")
	int updateTransacoesPagamentosRemetentesInvalidos(@Param("anoArquivo") int anoArquivo);
	
	/*@Modifying(clearAutomatically = true)
	@Query("UPDATE TransacaoFinanceira t SET t.valido = false, t.erroValidacao = 3 WHERE t.id IN "
			+ "(SELECT tf.id FROM TransacaoFinanceira tf WHERE tf.tipoTransacao = TipoTransacao.PAGAMENTO AND tf.remetente != RemetenteTransacao.FINANCEIRO AND tf.anoArquivoano=:ano)")
	int updateTransacoesPagamentosRemetentesInvalidos(@Param("ano") int ano);
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE TransacaoFinanceira t SET t.valido = false, t.erroValidacao = 1 WHERE t.id IN "
			+ "(SELECT tf.id FROM TransacaoFinanceira tf WHERE tf.tipoTransacao = TipoTransacao.PAGAMENTO AND tf.remetente != RemetenteTransacao.FINANCEIRO AND tf.anoArquivoano=:ano)")
	int updateTransacoesPagamentosRemetentesInvalidos(@Param("ano") int ano);*/
}
