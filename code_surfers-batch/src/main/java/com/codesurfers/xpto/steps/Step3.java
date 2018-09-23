package com.codesurfers.xpto.steps;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codesurfers.xpto.repository.TransacaoFinanceiraRepository;

@Configuration
public class Step3 {

	@Autowired
	private TransacaoFinanceiraRepository transacaoFinanceiraRepository;

	@Bean
	@StepScope
	public Tasklet aplicarRegras() {
		return new Tasklet() {
			@Value("#{jobParameters['ano']}")
			private Integer ano;

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				transacaoFinanceiraRepository.updatePagamentosRemetentesInvalidos(ano);
				transacaoFinanceiraRepository.updatePagamentosInvalidos(ano);
				transacaoFinanceiraRepository.updateRetiradasInvalidas(ano);
				transacaoFinanceiraRepository.updateDepositosInvalidos(ano);
				return RepeatStatus.FINISHED;
			}
		};
	}

}
