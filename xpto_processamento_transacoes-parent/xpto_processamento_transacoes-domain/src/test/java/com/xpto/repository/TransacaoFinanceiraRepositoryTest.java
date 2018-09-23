package com.xpto.repository;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpto.model.TransacaoFinanceira;
import com.xpto.model.enumerations.ErroValidacaoTransacao;
import com.xpto.model.enumerations.TipoTransacao;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransacaoFinanceiraRepositoryTest {

	@Autowired
	private TransacaoFinanceiraRepository transacaoFincanceiraRepository;

	@Value("classpath:dataset.json")
	Resource stateFile;

	private static final int ANO_ARQUIVO = 2018;

	@Before
	public void setup() throws JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		List<TransacaoFinanceira> transacoes = objectMapper.readValue(this.stateFile.getInputStream(),
				new TypeReference<List<TransacaoFinanceira>>() {
				});

		transacaoFincanceiraRepository.saveAll(transacoes);

		assertEquals(17, transacaoFincanceiraRepository.count());

		assertEquals(0L, transacaoFincanceiraRepository
				.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_001, ANO_ARQUIVO).longValue());

		assertEquals(0L, transacaoFincanceiraRepository
				.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_003, ANO_ARQUIVO).longValue());

		assertEquals(0L, transacaoFincanceiraRepository
				.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_004, ANO_ARQUIVO).longValue());

	}

	@Test
	public void testeUpdatePagamentosRemetentesInvalidos() {
		Integer expected = transacaoFincanceiraRepository.updatePagamentosRemetentesInvalidos(ANO_ARQUIVO);
		assertEquals(2, expected.intValue());
		assertEquals(2L, transacaoFincanceiraRepository
				.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_001, ANO_ARQUIVO).longValue());
		assertEquals(15L, transacaoFincanceiraRepository.countByValidoTrue().longValue());
	}

	@Test
	public void testeUpdateRetiradasInvalidas() {
		Integer expected = transacaoFincanceiraRepository.updateRetiradasInvalidas(ANO_ARQUIVO);
		assertEquals(2, expected.intValue());
		assertEquals(2, transacaoFincanceiraRepository
				.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_003, ANO_ARQUIVO).intValue());
		assertEquals(15L, transacaoFincanceiraRepository.countByValidoTrue().longValue());
	}

	@Test
	public void testeUpdateDepositosInvalidos() {
		Integer expected = transacaoFincanceiraRepository.updateDepositosInvalidos(ANO_ARQUIVO);
		assertEquals(2, expected.intValue());
		assertEquals(2, transacaoFincanceiraRepository
				.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_004, ANO_ARQUIVO).intValue());
		assertEquals(15, transacaoFincanceiraRepository.countByValidoTrue().longValue());
	}

	@Test
	public void testeUpdatePagamentosInvalidos() {
		Integer expected = transacaoFincanceiraRepository.updatePagamentosInvalidos(ANO_ARQUIVO);
		assertEquals(3, expected.intValue());
		assertEquals(3L, transacaoFincanceiraRepository
				.countByErroValidacaoAndAnoArquivo(ErroValidacaoTransacao.ERRO_002, ANO_ARQUIVO).intValue());
		assertEquals(14L, transacaoFincanceiraRepository.countByValidoTrue().longValue());
	}

	@Test
	public void testeDadosInseridosCorretamente() {
		List<TransacaoFinanceira> transacoes = transacaoFincanceiraRepository
				.findByTipoTransacaoAndAnoArquivo(TipoTransacao.DEPOSITO, ANO_ARQUIVO);
		assertEquals(2, transacoes.size());

		transacoes = transacaoFincanceiraRepository.findByTipoTransacaoAndAnoArquivo(TipoTransacao.FATURA, ANO_ARQUIVO);
		assertEquals(6, transacoes.size());

		transacoes = transacaoFincanceiraRepository.findByTipoTransacaoAndAnoArquivo(TipoTransacao.PAGAMENTO,
				ANO_ARQUIVO);
		assertEquals(6, transacoes.size());

		transacoes = transacaoFincanceiraRepository.findByTipoTransacaoAndAnoArquivo(TipoTransacao.RETIRADA,
				ANO_ARQUIVO);
		assertEquals(3, transacoes.size());

	}

	@Test
	public void testarPaginacaoTotal() {
		Pageable pageableRequest = PageRequest.of(0, 5);
		Page<TransacaoFinanceira> paginaTransacoes = transacaoFincanceiraRepository.findPaginacao(true,pageableRequest);
		assertEquals(5, paginaTransacoes.getContent().size());
	}

	@Test
	public void testarPaginacaoElementosPagina1() {
		Pageable pageableRequest = PageRequest.of(0, 5);
		Page<TransacaoFinanceira> paginaTransacoes = transacaoFincanceiraRepository.findPaginacao(true,pageableRequest);
		assertEquals("2d500b5a-7fe6-4a9c-9569-06691b978ce9", paginaTransacoes.getContent().get(0).getId());
		assertEquals("ca8f383d-ee47-4f39-b110-d994cf0f4c00",
				paginaTransacoes.getContent().get(paginaTransacoes.getContent().size() - 1).getId());
	}

	@Test
	public void testarPaginacaoElementosPagina2() {
		Pageable pageableRequest = PageRequest.of(1, 5);
		Page<TransacaoFinanceira> paginaTransacoes = transacaoFincanceiraRepository.findPaginacao(true,pageableRequest);
		assertEquals("70dc1b6d-ef98-4464-b19d-dd0843eb5eaf", paginaTransacoes.getContent().get(0).getId());
		assertEquals("5e09e19e-58a7-49cb-a5dc-73a25701e0e9",
				paginaTransacoes.getContent().get(paginaTransacoes.getContent().size() - 1).getId());
	}

}
