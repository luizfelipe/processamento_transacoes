package com.codesurfers.xpto.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.codesurfers.xpto.model.TransacaoFinanceira;
import com.codesurfers.xpto.model.enumerations.ErroValidacaoTransacao;
import com.codesurfers.xpto.model.enumerations.TipoTransacao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransacaoFinanceiraRepositoryTest {

	@Autowired
	private TransacaoFinanceiraRepository transacaoFincanceiraRepository;

	@Value("classpath:dataset.json")
	Resource stateFile;
	
	
	private final int ANO_ARQUIVO = 2018;

	@Before
	public void setup() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		List<TransacaoFinanceira> transacoes = objectMapper.readValue(this.stateFile.getInputStream(),
				new TypeReference<List<TransacaoFinanceira>>() {
				});
		
		transacaoFincanceiraRepository.saveAll(transacoes);
		
		assertEquals(17, transacaoFincanceiraRepository.count());
		
		assertEquals(new Long(0), transacaoFincanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_001, ANO_ARQUIVO));
		
		assertEquals(new Long(0), transacaoFincanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_003, ANO_ARQUIVO));
		
		assertEquals(new Long(0), transacaoFincanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_004, ANO_ARQUIVO));
		
		
	}
	
	@Test
	public void testeUpdatePagamentosRemetentesInvalidos() {
		Integer expected = transacaoFincanceiraRepository.updatePagamentosRemetentesInvalidos(ANO_ARQUIVO);
		assertEquals(new Integer(2),  expected);
		assertEquals(new Long(2), transacaoFincanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_001, ANO_ARQUIVO));
		assertEquals(new Long(15), transacaoFincanceiraRepository.countByValidoTrue());
	}
	
	@Test
	public void testeUpdateRetiradasInvalidas() {
		Integer expected = transacaoFincanceiraRepository.updateRetiradasInvalidas(ANO_ARQUIVO);
		assertEquals(new Integer(2),  expected);
		assertEquals(new Long(2), transacaoFincanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_003, ANO_ARQUIVO));
		assertEquals(new Long(15), transacaoFincanceiraRepository.countByValidoTrue());
	}
	
	@Test
	public void testeUpdateDepositosInvalidos() {
		Integer expected = transacaoFincanceiraRepository.updateDepositosInvalidos(ANO_ARQUIVO);
		assertEquals(new Integer(2),  expected);
		assertEquals(new Long(2), transacaoFincanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_004, ANO_ARQUIVO));
		assertEquals(new Long(15), transacaoFincanceiraRepository.countByValidoTrue());
	}
	
	
	@Test
	public void testeUpdatePagamentosInvalidos() {
		Integer expected = transacaoFincanceiraRepository.updatePagamentosInvalidos(ANO_ARQUIVO);
		assertEquals(new Integer(3),  expected);
		assertEquals(new Long(3), transacaoFincanceiraRepository.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_002, ANO_ARQUIVO));
		assertEquals(new Long(14), transacaoFincanceiraRepository.countByValidoTrue());
	}
	
	@Test
	public void testeDadosInseridosCorretamente() {
		List<TransacaoFinanceira> transacoes = transacaoFincanceiraRepository.findByTipoTransacaoAndAnoArquivo(TipoTransacao.DEPOSITO, ANO_ARQUIVO);
		assertEquals(new Integer(2), new Integer(transacoes.size()));
		
		transacoes = transacaoFincanceiraRepository.findByTipoTransacaoAndAnoArquivo(TipoTransacao.FATURA, ANO_ARQUIVO);
		assertEquals(new Integer(6), new Integer(transacoes.size()));
		
		transacoes = transacaoFincanceiraRepository.findByTipoTransacaoAndAnoArquivo(TipoTransacao.PAGAMENTO, ANO_ARQUIVO);
		assertEquals(new Integer(6), new Integer(transacoes.size()));
		
		transacoes = transacaoFincanceiraRepository.findByTipoTransacaoAndAnoArquivo(TipoTransacao.RETIRADA, ANO_ARQUIVO);
		assertEquals(new Integer(3), new Integer(transacoes.size()));
		
	}
	
	@Test
	public void testarPaginacaoTotal() {
		Pageable pageableRequest = PageRequest.of(0, 5);
		Page<TransacaoFinanceira> paginaTransacoes = transacaoFincanceiraRepository.findByValidoTrue(pageableRequest);
		assertEquals(new Integer(5), new Integer(paginaTransacoes.getContent().size()));
	}
	
	@Test
	public void testarPaginacaoElementosPagina1() {
		Pageable pageableRequest = PageRequest.of(0, 5);
		Page<TransacaoFinanceira> paginaTransacoes = transacaoFincanceiraRepository.findByValidoTrue(pageableRequest);
		assertEquals("2d500b5a-7fe6-4a9c-9569-06691b978ce9", paginaTransacoes.getContent().get(0).getId());
		assertEquals("ca8f383d-ee47-4f39-b110-d994cf0f4c00", paginaTransacoes.getContent().get(paginaTransacoes.getContent().size()-1).getId());
	}
	
	@Test
	public void testarPaginacaoElementosPagina2() {
		Pageable pageableRequest = PageRequest.of(1, 5);
		Page<TransacaoFinanceira> paginaTransacoes = transacaoFincanceiraRepository.findByValidoTrue(pageableRequest);
		assertEquals("70dc1b6d-ef98-4464-b19d-dd0843eb5eaf", paginaTransacoes.getContent().get(0).getId());
		assertEquals("5e09e19e-58a7-49cb-a5dc-73a25701e0e9", paginaTransacoes.getContent().get(paginaTransacoes.getContent().size()-1).getId());
	}
	
	
	

}
