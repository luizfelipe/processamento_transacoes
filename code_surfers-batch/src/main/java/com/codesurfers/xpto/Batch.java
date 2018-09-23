package com.codesurfers.xpto;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.codesurfers.xpto.model.TransacaoFinanceira;

@Configuration
@EnableBatchProcessing
public class Batch {

	@Bean
	public Job job(JobBuilderFactory jobs, StepBuilderFactory steps, Step1 step1, Step2 step2, Step3 step3,
			EntityManagerFactory entityManagerFactory) throws MalformedURLException {

		Step s1 = steps.get("baixar_descompactar").tasklet(step1.baixarEDescompactarArquivo()).build();

		Step slave = steps.get("processar_arquivo").<TransacaoFinanceira, TransacaoFinanceira>chunk(1000)
				.reader(step2.reader(null, null)).writer(step2.writer(entityManagerFactory)).build();

		TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
		retVal.setTaskExecutor(new SimpleAsyncTaskExecutor());
		retVal.setStep(slave);
		retVal.setGridSize(3);

		Step s2 = steps.get("partitionStep").partitioner("carregar_arquivo_banco", getParticionador())
				.partitionHandler(retVal).build();

		Step s3 = steps.get("aplicar_regras").tasklet(step3.aplicarRegras()).build();

		return jobs.get("job").incrementer(new RunIdIncrementer())
				.validator(new DefaultJobParametersValidator(new String[] { "ano" }, new String[] {})).start(s1)
				.next(s2).next(s3).build();
	}

	@Bean
	@StepScope
	public MultiResourcePartitioner getParticionador() {
		MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources;
		try {
			resources = resolver.getResources("file:data/transacoes_partition*.csv");
			partitioner.setResources(resources);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return partitioner;
	}

}