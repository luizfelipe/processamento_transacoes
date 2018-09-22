package com.codesurfers.xpto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesurfers.xpto.model.TransacaoFinanceira;

public interface TransacaoFinanceiraRepository extends JpaRepository<TransacaoFinanceira, String> {

	List<TransacaoFinanceira> findByValidoTrue();
}
