package com.xpto.steps;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xpto.tasklets.FileTasklet;

@Configuration
public class Step1 {

	@Bean
	@StepScope
	public Tasklet baixarEDescompactarArquivo() {
		return new FileTasklet();
	}
}
