package com.money.api.repository.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.money.api.model.Pessoa;

public interface PessoaRepositoryQuery {

	public Page<Pessoa> filtrarPessoa(String nome, Pageable pag);
	
}
