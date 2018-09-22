package com.codesurfers.xpto;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.codesurfers.xpto.model.TransacaoFinanceira;

@Configuration
@EnableBatchProcessing
public class Batch {

	@Bean
	public Job job(JobBuilderFactory jobs, StepBuilderFactory steps, Step1 step1, Step2 step2, Step3 step3,
			EntityManagerFactory entityManagerFactory) {

		Step s1 = steps.get("baixar_descompactar").tasklet(step1.baixarEDescompactarArquivo()).build();

		Step slave = steps.get("processar_arquivo").<TransacaoFinanceira, TransacaoFinanceira>chunk(1000)
				.reader(step2.reader()).writer(step2.writer(entityManagerFactory)).build();

		TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
		retVal.setTaskExecutor(taskExecutor());
		retVal.setStep(slave);
		retVal.setGridSize(3);

		Step s2 = steps.get("partitionStep").partitioner("carregar_arquivo_banco", new Particionador())
				.partitionHandler(retVal).build();

		Step s3 = steps.get("aplicar_regras").tasklet(step3.aplicarRegras()).build();

		return jobs.get("job").incrementer(new RunIdIncrementer()).start(s1).next(s2).next(s3).build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(25);
		return executor;
	}
}