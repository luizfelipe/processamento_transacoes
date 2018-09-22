package com.codesurfers.xpto.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class TransacaoFinanceiraDAO {

	@PersistenceContext
	EntityManager em;
	
	public int atualizarPagamentosRemetentesInvalidos(int ano) {
		Query query = em.createNativeQuery(
				"update transacao_financeira \r\n" + 
				"	set valido = false, erro_validacao = 1 \r\n" + 
				"	where transacao_financeira.id in (select id from transacao_financeira where tipo_transacao = 'PAGAMENTO' and remetente <> 'FINANCEIRO' and ano=:ano)"
		);
		query.setParameter("ano", ano);
		return query.executeUpdate();
	}
	
	public int atualizarRetiradasInvalidas(int ano) {
		return 0;
	}
	
	public int atualizarDepositosInvalidos(int ano) {
		return 0;
	}
}
