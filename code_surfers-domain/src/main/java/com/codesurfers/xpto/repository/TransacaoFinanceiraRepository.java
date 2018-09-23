package com.codesurfers.xpto.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.codesurfers.xpto.model.TransacaoFinanceira;
import com.codesurfers.xpto.model.enumerations.ErroValidacaoTransacao;
import com.codesurfers.xpto.model.enumerations.TipoTransacao;

public interface TransacaoFinanceiraRepository extends PagingAndSortingRepository<TransacaoFinanceira, String> {
	/**
	 * Retorna uma lista paginada das transacoes
	 * @param pageable pagina
	 * @return lista de transacoes
	 */
	Page<TransacaoFinanceira> findByValidoTrue(Pageable pageable);
	
	/**
	 * Conta quantas transacoes sao validas
	 * @return
	 */
	Long countByValidoTrue();
	
	/**
	 * Lista transacoes por tipo e ano que o arquivo foi carregado
	 * @param tipoTransacao tipo
	 * @param ano ano do arquivo
	 * @return lista de transacoes
	 */
	List<TransacaoFinanceira> findByTipoTransacaoAndAnoArquivo(TipoTransacao tipoTransacao, int ano);
	
	
	/**
	 * Conta quantas transacoes estao erradas por ano
	 * @param erro tipo de erro
	 * @param ano que o arquvo foi carregado
	 * @return lista de transacoes
	 */
	Long countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao erro, int ano);	
	
	/**
	 * Aplicando a regra numero 1 ( Apenas o departamento Financeiro pode emitir pagamentos para terceiros, sendo necessário validar as transações em busca de pagamentos indevidos)
	 * @param anoArquivo ano que os dados foram importados
	 * @return numero de registros afetados
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	int updatePagamentosRemetentesInvalidos(@Param("anoArquivo") int anoArquivo);
	
	/**
	 * Aplicando a regra numero 3 ( Apenas o departamento Comercial está autorizado a realizar retiradas de valores, que não podem ser superiores a R$ 10.000 )
	 * @param anoArquivo ano que os dados foram importados
	 * @return numero de registros afetados
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	int updateRetiradasInvalidas(@Param("anoArquivo") int anoArquivo);
	
	
	/**
	 * Aplicando a regra numero 4 (Nenhum departamento está autorizado a realizar depósitos em nome da XPTO)
	 * @param anoArquivo ano que os dados foram importados
	 * @return numero de registros afetados
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	int updateDepositosInvalidos(@Param("anoArquivo") int anoArquivo);
	
	/**
	 * Aplicando a regra numero 2 (Todos os pagamentos realizados devem estar associados a uma fatura emitida pelos departamentos de TI, Operações e Administrativo e  Os pagamentos oriundos de uma fatura devem possuir os mesmos dados nos campos valor e destinatário. Adicionalmente, a data do pagamento será equivalente ao dia seguinte da fatura original.)
	 * @param anoArquivo ano que o arquivo foi carregado
	 * @return numero de registros afetados
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(nativeQuery = true)
	int updatePagamentosInvalidos(@Param("anoArquivo") int anoArquivo);
	
}
