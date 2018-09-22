package com.codesurfers.xpto;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codesurfers.xpto.model.Transacao;

@Configuration
@EnableBatchProcessing
public class Batch {

	@Bean
	public Job job(JobBuilderFactory jobs, StepBuilderFactory steps, Step1 step1, Step2 step2) {

		Step s1 = steps.get("baixar_descompactar").tasklet(step1.baixarEDescompactarArquivo()).build();

		Step s2 = steps.get("processar_arquivo").<Transacao, Transacao>chunk(1000).reader(step2.reader())
				.processor(step2.processor()).writer(step2.writer()).build();

		return jobs.get("job").incrementer(new RunIdIncrementer()).start(s1).next(s2).build();
	}
}