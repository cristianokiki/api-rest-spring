package com.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.api.model.Pessoa;
import com.money.api.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

}
