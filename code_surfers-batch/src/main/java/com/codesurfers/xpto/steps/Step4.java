package com.codesurfers.xpto.steps;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.codesurfers.xpto.model.TransacaoFinanceira;

@Configuration
public class Step4 {

	@Bean(name = "step4-reader")
	@StepScope
	public JpaPagingItemReader<TransacaoFinanceira> reader(EntityManagerFactory entityManagerFactory,
			@Value("#{jobParameters['ano']}") Integer ano) throws Exception  {
		JpaPagingItemReader<TransacaoFinanceira> itemReader = new JpaPagingItemReader<>();
		String query = "SELECT tf FROM TransacaoFinanceira tf WHERE tf.valido = false and tf.anoArquivo = " + ano
				+ " ORDER BY tf.erroValidacao";
		itemReader.setQueryString(query);
		itemReader.setEntityManagerFactory(entityManagerFactory);
		itemReader.setPageSize(1000);
		itemReader.afterPropertiesSet();
		itemReader.setSaveState(true);
		return itemReader;
	}

	@Bean(name = "step4-writer")
	@StepScope
	public FlatFileItemWriter<TransacaoFinanceira> writer() {
		FlatFileItemWriter<TransacaoFinanceira> flatFileItemWriter = new FlatFileItemWriter<>();
		StringBuilder sb = new StringBuilder();
		sb.append("log/").append(new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())).append(".txt");
		flatFileItemWriter.setResource(new FileSystemResource(sb.toString()));
		BeanWrapperFieldExtractor<TransacaoFinanceira> bwfe = new BeanWrapperFieldExtractor<>();
		bwfe.setNames(new String[] { "id", "erroValidacao.descricao" });
		DelimitedLineAggregator<TransacaoFinanceira> dla = new DelimitedLineAggregator<>();
		dla.setDelimiter(" = ");
		dla.setFieldExtractor(bwfe);
		flatFileItemWriter.setLineAggregator(dla);
		flatFileItemWriter.setAppendAllowed(true);
		flatFileItemWriter.setShouldDeleteIfEmpty(true);
		return flatFileItemWriter;
	}

}
