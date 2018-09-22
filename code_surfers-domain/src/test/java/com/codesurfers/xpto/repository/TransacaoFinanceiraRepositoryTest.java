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
import org.springframework.test.context.junit4.SpringRunner;

import com.codesurfers.xpto.model.TransacaoFinanceira;
import com.codesurfers.xpto.model.enumerations.ErroValidacaoTransacao;
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
		
		assertEquals(10, transacaoFincanceiraRepository.count());
		
		assertEquals(new Long(0), transacaoFincanceiraRepository.countByErroValidacao(ErroValidacaoTransacao.ERRO_001));
		
		assertEquals(new Long(0), transacaoFincanceiraRepository.countByErroValidacao(ErroValidacaoTransacao.ERRO_003));
		
		assertEquals(new Long(0), transacaoFincanceiraRepository.countByErroValidacao(ErroValidacaoTransacao.ERRO_004));
		
		
	}
	
	@Test
	public void testeUpdatePagamentosRemetentesInvalidos() {
		transacaoFincanceiraRepository.updatePagamentosRemetentesInvalidos(ANO_ARQUIVO);
		assertEquals(new Long(2), transacaoFincanceiraRepository.countByErroValidacao(ErroValidacaoTransacao.ERRO_001));

	}
	
	@Test
	public void testeUpdateRetiradasInvalidas() {
		transacaoFincanceiraRepository.updateRetiradasInvalidas(ANO_ARQUIVO);
		assertEquals(new Long(2), transacaoFincanceiraRepository.countByErroValidacao(ErroValidacaoTransacao.ERRO_003));
	}
	
	@Test
	public void testeupdateDepositosInvalidos() {
		transacaoFincanceiraRepository.updateDepositosInvalidos(ANO_ARQUIVO);
		assertEquals(new Long(2), transacaoFincanceiraRepository.countByErroValidacao(ErroValidacaoTransacao.ERRO_004));
	}
	
	

}
