package com.codesurfers.xpto.steps;

import java.net.MalformedURLException;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
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

	@Bean
	@StepScope
	public FlatFileItemReader<TransacaoFinanceira> reader(@Value("#{stepExecutionContext['fileName']}") String file,
			@Value("#{jobParameters['ano']}") Integer ano) throws MalformedURLException {
		FlatFileItemReader<TransacaoFinanceira> reader = new FlatFileItemReader<TransacaoFinanceira>();
		reader.setResource(new UrlResource(file));
		reader.setStrict(true);
		reader.setLineMapper(new DefaultLineMapper<TransacaoFinanceira>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer(";") {
					{
						setNames(new String[] { "ID", "TIMESTAMP", "REMETENTE", "DESTINATARIO", "VALOR",
								"TIPOTRANSACAO" });
					}
				});
				setFieldSetMapper(new TransacaoMapper(ano));
			}
		});
		return reader;
	}

	@Bean
	public ItemWriter<TransacaoFinanceira> writer(EntityManagerFactory entityManagerFactory) {
		JpaItemWriter<TransacaoFinanceira> writer = new JpaItemWriter<TransacaoFinanceira>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}

}
