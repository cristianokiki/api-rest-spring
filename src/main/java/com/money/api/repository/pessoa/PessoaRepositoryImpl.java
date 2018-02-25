package com.money.api.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.money.api.model.Pessoa;
import com.money.api.model.Pessoa_;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

	@Autowired
	private EntityManager manager;

	@Override
	public Page<Pessoa> filtrarPessoa(String nome, Pageable pag) {
		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> query = builder.createQuery(Pessoa.class);
		Root<Pessoa> root = query.from(Pessoa.class);

		Predicate[] predicates = criarRestrincoes(nome, root, builder);
		query.where(predicates);

		TypedQuery<Pessoa> typedQuery = this.manager.createQuery(query);

		adicionarRestricaoPaginacao(typedQuery, pag);

		Long total = totalRegistroRestrincao(nome);
		
		return new PageImpl<>(typedQuery.getResultList(), pag, total);
	}

	private Long totalRegistroRestrincao(String nome) {
		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Pessoa> root = query.from(Pessoa.class);
		
		Predicate[] restrincoes = this.criarRestrincoes(nome, root, builder);
		
		query.where(restrincoes);
		
		query.select(builder.count(root));
		
		TypedQuery<Long> typedQuery = this.manager.createQuery(query);
		
		
		return typedQuery.getSingleResult();
	}

	private void adicionarRestricaoPaginacao(TypedQuery<Pessoa> typedQuery, Pageable pag) {
		int numeroPag = pag.getPageNumber();
		int totalPorPag = pag.getPageSize();

		int primeiroRegistro = numeroPag * totalPorPag;

		typedQuery.setFirstResult(primeiroRegistro);
		typedQuery.setMaxResults(totalPorPag);

	}

	private Predicate[] criarRestrincoes(String nome, Root<Pessoa> root, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(nome)) {
			predicates.add(builder.like(builder.lower(root.get(Pessoa_.nome)), "%" + nome + "%"));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
