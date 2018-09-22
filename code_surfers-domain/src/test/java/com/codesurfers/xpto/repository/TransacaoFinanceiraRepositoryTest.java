package com.codesurfers.xpto.repository;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import com.codesurfers.xpto.model.TransacaoFinanceira;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransacaoFinanceiraRepositoryTest {

	@Autowired
	private TransacaoFinanceiraRepository entidade;

	@Value("classpath:dataset.json")
	Resource stateFile;

	@Test
	@Ignore
	public void teste1_incluir() {
		TransacaoFinanceira t = new TransacaoFinanceira();
		t.setId("123");
		t.setDataHora(Calendar.getInstance());
		entidade.save(t);
	}

	@Test
	@Ignore
	public void teste2_buscar() {
		TransacaoFinanceira t = entidade.findById("123").get();
		assertNotNull(t);

	}

	@Test
	public void teste() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		TransacaoFinanceira t = null;
		try {
			t = objectMapper.readValue(this.stateFile.getInputStream(), TransacaoFinanceira.class);
		} catch (IOException e) {
			// throw new Exception();
		}

	}
}
