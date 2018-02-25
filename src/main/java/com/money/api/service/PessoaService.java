package com.money.api.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.money.api.model.Pessoa;
import com.money.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa adicionar(Pessoa pessoa) {
		return this.pessoaRepository.save(pessoa);
	}

	public Pessoa atualizar(Pessoa pessoa, Long codigo) {

		Pessoa pessoaSalva = this.buscarPorId(codigo);

		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
		return this.pessoaRepository.save(pessoaSalva);
	}

	public void atualizarStatusAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoa = this.buscarPorId(codigo);
		pessoa.setAtivo(ativo);
		this.pessoaRepository.save(pessoa);
	}

	public Pessoa buscarPorId(Long codigo) {

		Pessoa pessoa = this.pessoaRepository.findOne(codigo);

		if (pessoa == null) {
			throw new EmptyResultDataAccessException(1);
		}

		return pessoa;
	}

	public List<Pessoa> buscarTodas() {
		return this.pessoaRepository.findAll();
	}

	public void apagar(Long codigo) {
		this.pessoaRepository.delete(codigo);

	}

	public Page<Pessoa> filtrarPessoa(String nome, Pageable pag) {
		return this.pessoaRepository.filtrarPessoa(nome, pag);

	}

}
