package com.money.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.money.api.model.Categoria_;
import com.money.api.model.Lancamento;
import com.money.api.model.Lancamento_;
import com.money.api.model.Pessoa_;
import com.money.api.repository.filter.LancamentoFilter;
import com.money.api.repository.projection.ResumoLancamento;

@Repository
public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable) {

		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> query = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = query.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(filter, builder, root);

		query.where(predicates);

		TypedQuery<Lancamento> typeQuery = this.manager.createQuery(query);

		adicionarRestricaoDePaginacao(typeQuery, pageable);

		Long total = this.totalRegistro(filter);

		return new PageImpl<>(typeQuery.getResultList(), pageable, total);
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter filter, Pageable pageable) {
		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> query = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = query.from(Lancamento.class);

		query.select(builder.construct(ResumoLancamento.class, root.get(Lancamento_.codigo),
				root.get(Lancamento_.descricao), root.get(Lancamento_.dataVencimento),
				root.get(Lancamento_.dataPagamento), root.get(Lancamento_.valor), root.get(Lancamento_.tipo),
				root.get(Lancamento_.categoria).get(Categoria_.nome), root.get(Lancamento_.pessoa).get(Pessoa_.nome)));

		Predicate[] predicates = criarRestricoes(filter, builder, root);

		query.where(predicates);

		TypedQuery<ResumoLancamento> typeQuery = this.manager.createQuery(query);

		adicionarRestricaoDePaginacao(typeQuery, pageable);

		Long total = this.totalRegistro(filter);

		return new PageImpl<>(typeQuery.getResultList(), pageable, total);
	}

	private void adicionarRestricaoDePaginacao(TypedQuery<?> typeQuery, Pageable pageable) {
		int pagAtual = pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();
		int primeiroRegistro = pagAtual * totalRegistroPorPagina;

		typeQuery.setFirstResult(primeiroRegistro);
		typeQuery.setMaxResults(totalRegistroPorPagina);
	}

	private Long totalRegistro(LancamentoFilter filter) {
		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(filter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));

		return manager.createQuery(criteria).getSingleResult();
	}

	private Predicate[] criarRestricoes(LancamentoFilter filter, CriteriaBuilder builder, Root<Lancamento> root) {
		List<Predicate> lista = new ArrayList<>();

		if (!StringUtils.isEmpty(filter.getDescricao())) {
			lista.add(builder.like(builder.lower(root.get(Lancamento_.descricao)),
					"%" + filter.getDescricao().toLowerCase() + "%"));
		}

		if (filter.getDataVencimentoDe() != null) {
			lista.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), filter.getDataVencimentoDe()));
		}

		if (filter.getDataVencimentoAte() != null) {
			lista.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), filter.getDataVencimentoAte()));
		}

		return lista.toArray(new Predicate[lista.size()]);
	}

}
