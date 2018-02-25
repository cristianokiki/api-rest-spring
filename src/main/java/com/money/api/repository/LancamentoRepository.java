package com.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.api.model.Lancamento;
import com.money.api.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
