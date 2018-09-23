package com.xpto;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.xpto.model.TransacaoFinanceira;
import com.xpto.steps.Step1;
import com.xpto.steps.Step2;
import com.xpto.steps.Step3;
import com.xpto.steps.Step4;

@Configuration
@EnableBatchProcessing
@PropertySource("file:batch-config/xpto.properties")
public class Batch {

	@Bean
	public Job job(JobBuilderFactory jobs, StepBuilderFactory steps, Step1 step1, Step2 step2, Step3 step3, Step4 step4,
			EntityManagerFactory entityManagerFactory) throws Exception {

		Step s1 = steps.get("baixar_descompactar").tasklet(step1.baixarEDescompactarArquivo()).build();

		Step slave = steps.get("processar_arquivo").<TransacaoFinanceira, TransacaoFinanceira>chunk(1000)
				.reader(step2.reader(null, null)).writer(step2.writer(entityManagerFactory)).build();

		TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
		retVal.setTaskExecutor(new SimpleAsyncTaskExecutor());
		retVal.setStep(slave);
		retVal.setGridSize(5);

		Step s2 = steps.get("partitionStep").partitioner("carregar_arquivo_banco", getParticionador())
				.partitionHandler(retVal).build();

		Step s3 = steps.get("aplicar_regras").tasklet(step3.aplicarRegras()).build();

		Step s4 = steps.get("gerar_log").<TransacaoFinanceira, TransacaoFinanceira>chunk(1000)
				.reader(step4.reader(entityManagerFactory, null)).writer(step4.writer()).build();

		return jobs.get("job").validator(new DefaultJobParametersValidator(new String[] { "ano" }, new String[] {}))
				.start(s1).next(s2).next(s3).next(s4).build();
	}

	@Bean
	@StepScope
	public MultiResourcePartitioner getParticionador() throws IOException {
		MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("file:data/transacoes_partition*.csv");
		partitioner.setResources(resources);

		return partitioner;
	}

}