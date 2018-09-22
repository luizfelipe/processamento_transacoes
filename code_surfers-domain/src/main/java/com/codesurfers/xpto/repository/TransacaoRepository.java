package com.codesurfers.xpto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesurfers.xpto.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, String> {

}
