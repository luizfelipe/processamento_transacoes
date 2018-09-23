package com.codesurfers.xpto;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

import com.codesurfers.xpto.model.TransacaoFinanceira;
import com.codesurfers.xpto.model.enumerations.ErroValidacaoTransacao;
import com.codesurfers.xpto.model.enumerations.RemetenteTransacao;
import com.codesurfers.xpto.model.enumerations.TipoTransacao;

public class TransacaoMapper implements FieldSetMapper<TransacaoFinanceira> {

	private static final String ID = "ID";
	private static final String TIMESTAMP = "TIMESTAMP";
	private static final String REMETENTE = "REMETENTE";
	private static final String DESTINATARIO = "DESTINATARIO";
	private static final String VALOR = "VALOR";
	private static final String TIPOTRANSACAO = "TIPOTRANSACAO";
	
	private Integer ano;

	public TransacaoMapper(Integer ano) {
		this.ano = ano;
	}

	@Override
	public TransacaoFinanceira mapFieldSet(FieldSet fieldSet) throws BindException {
		TransacaoFinanceira transacao = new TransacaoFinanceira();
		transacao.setId(fieldSet.readString(ID));
		transacao.setDataHora(fromISO8601UTC(fieldSet.readString(TIMESTAMP)));
		transacao.setRemetente(RemetenteTransacao.valueOf(unaccent(fieldSet.readString(REMETENTE))));
		transacao.setDestinatario(fieldSet.readString(DESTINATARIO));
		transacao.setValor(fieldSet.readDouble(VALOR));
		transacao.setTipoTransacao(TipoTransacao.valueOf(fieldSet.readString(TIPOTRANSACAO)));
		transacao.setValido(true);
		transacao.setErroValidacao(ErroValidacaoTransacao.SEM_ERRO);
		transacao.setAnoArquivo(ano);
		return transacao;
	}

	public static Calendar fromISO8601UTC(String dateStr) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		df.setTimeZone(tz);
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(dateStr));
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String unaccent(String s) {
		if (StringUtils.isEmpty(s)) {
			return null;
		}
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

}
