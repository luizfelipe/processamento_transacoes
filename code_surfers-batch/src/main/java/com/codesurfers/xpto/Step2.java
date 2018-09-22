package com.codesurfers.xpto;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.codesurfers.xpto.model.TransacaoFinanceira;

@Configuration
public class Step2 {

	@Bean
	@StepScope
	public ItemReader<TransacaoFinanceira> reader() {
		MultiResourceItemReader<TransacaoFinanceira> reader = new MultiResourceItemReader<TransacaoFinanceira>();
		reader.setResources(getResources());
		reader.setDelegate(delegate());
		return reader;
	}

	@Bean
	public FlatFileItemReader<TransacaoFinanceira> delegate() {
		FlatFileItemReader<TransacaoFinanceira> reader = new FlatFileItemReader<TransacaoFinanceira>();
		reader.setStrict(true);
		reader.setLineMapper(new DefaultLineMapper<TransacaoFinanceira>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer(";") {
					{
						setNames(new String[] { "ID", "TIMESTAMP", "REMETENTE", "DESTINATARIO", "VALOR",
								"TIPOTRANSACAO" });
					}
				});
				setFieldSetMapper(new TransacaoMapper());
			}
		});
		return reader;
	}

	@Bean
	public ItemProcessor<TransacaoFinanceira, TransacaoFinanceira> processor() {
		return new ItemProcessor<TransacaoFinanceira, TransacaoFinanceira>() {
			@Override
			public TransacaoFinanceira process(TransacaoFinanceira item) throws Exception {
				System.out.println("Processado");
				return item;
			}
		};
	}

	@Bean
	public ItemWriter<TransacaoFinanceira> writer(EntityManagerFactory entityManagerFactory) {
		JpaItemWriter<TransacaoFinanceira> writer = new JpaItemWriter<TransacaoFinanceira>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}
	
	private Resource[] getResources() {
		Resource[] inputResources = null;
		try {
			inputResources = new PathMatchingResourcePatternResolver().getResources("file:data/transacoes_partition*.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputResources;
	}

}
