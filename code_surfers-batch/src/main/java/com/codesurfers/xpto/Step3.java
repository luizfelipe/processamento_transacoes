package com.codesurfers.xpto;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codesurfers.xpto.repository.TransacaoFinanceiraRepository;

@Configuration
public class Step3 {
	
	@Autowired
	private TransacaoFinanceiraRepository transacaoFinanceiraRepository;
	
	@Bean
	public Tasklet aplicarRegras() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				transacaoFinanceiraRepository.updateDepositosInvalidos(2018);
				transacaoFinanceiraRepository.updatePagamentosRemetentesInvalidos(2018);
				transacaoFinanceiraRepository.updateRetiradasInvalidas(2018);
				return RepeatStatus.FINISHED;
			}
		};
	}

}
