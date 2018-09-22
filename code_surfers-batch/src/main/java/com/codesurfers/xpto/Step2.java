package com.codesurfers.xpto;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.codesurfers.xpto.model.TransacaoFinanceira;

@Configuration
public class Step2 {

	@Bean
	public ItemReader<TransacaoFinanceira> reader() {
		FlatFileItemReader<TransacaoFinanceira> reader = new FlatFileItemReader<TransacaoFinanceira>();
		reader.setStrict(true);
		reader.setLinesToSkip(1);
		reader.setResource(new FileSystemResource("data/transacoes.csv"));
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
	public ItemWriter<TransacaoFinanceira> writer() {
		return new ItemWriter<TransacaoFinanceira>() {
			@Override
			public void write(List<? extends TransacaoFinanceira> items) throws Exception {
				System.out.println("Escrevendo");
			}
		};
	}

}
