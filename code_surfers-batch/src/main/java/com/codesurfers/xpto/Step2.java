package com.codesurfers.xpto;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.codesurfers.xpto.model.TransacaoFinanceira;

@Configuration
public class Step2 {

	@Value("file:data/transacoes_partition*.csv")
	private Resource[] inputResources;

	public ItemReader<TransacaoFinanceira> reader() {
		MultiResourceItemReader<TransacaoFinanceira> reader = new MultiResourceItemReader<TransacaoFinanceira>();
		reader.setResources(inputResources);
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

	public Integer obterNumeroArquivos() {
		return inputResources.length;
	}

}
