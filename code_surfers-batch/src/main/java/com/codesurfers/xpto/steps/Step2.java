package com.codesurfers.xpto.steps;

import java.net.MalformedURLException;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;

import com.codesurfers.xpto.TransacaoMapper;
import com.codesurfers.xpto.model.TransacaoFinanceira;

@Configuration
public class Step2 {

	@Bean(name = "step2-reader")
	@StepScope
	public FlatFileItemReader<TransacaoFinanceira> reader(@Value("#{stepExecutionContext['fileName']}") String file,
			@Value("#{jobParameters['ano']}") Integer ano) throws MalformedURLException {
		FlatFileItemReader<TransacaoFinanceira> reader = new FlatFileItemReader<>();
		reader.setResource(new UrlResource(file));
		reader.setStrict(true);
		DefaultLineMapper<TransacaoFinanceira> dlm = new DefaultLineMapper<>();
		DelimitedLineTokenizer dlt = new DelimitedLineTokenizer(";");
		dlt.setNames(new String[] { "ID", "TIMESTAMP", "REMETENTE", "DESTINATARIO", "VALOR", "TIPOTRANSACAO" });
		dlm.setLineTokenizer(dlt);
		dlm.setFieldSetMapper(new TransacaoMapper(ano));
		reader.setLineMapper(dlm);
		return reader;
	}

	@Bean(name = "step2-writer")
	public JpaItemWriter<TransacaoFinanceira> writer(EntityManagerFactory entityManagerFactory) {
		JpaItemWriter<TransacaoFinanceira> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}

}
