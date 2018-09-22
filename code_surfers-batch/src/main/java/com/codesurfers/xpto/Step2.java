package com.codesurfers.xpto;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codesurfers.xpto.model.Transacao;

@Configuration
public class Step2 {

	@Bean
	public ItemReader<Transacao> reader() {
		return new ItemReader<Transacao>() {
			@Override
			public Transacao read()
					throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
				System.out.println("Lendo");
				return new Transacao();
			}
		};
	}

	@Bean
	public ItemProcessor<Transacao, Transacao> processor() {
		return new ItemProcessor<Transacao, Transacao>() {
			@Override
			public Transacao process(Transacao item) throws Exception {
				System.out.println("Processado");
				return item;
			}
		};
	}

	@Bean
	public ItemWriter<Transacao> writer() {
		return new ItemWriter<Transacao>() {
			@Override
			public void write(List<? extends Transacao> items) throws Exception {
				System.out.println("Escrevendo");
			}
		};
	}

}
