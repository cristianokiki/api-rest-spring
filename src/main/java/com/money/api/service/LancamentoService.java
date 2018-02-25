package com.money.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.money.api.exception.ValidationGenericException;
import com.money.api.model.Categoria;
import com.money.api.model.Lancamento;
import com.money.api.model.Pessoa;
import com.money.api.repository.CategoriaRepository;
import com.money.api.repository.LancamentoRepository;
import com.money.api.repository.PessoaRepository;
import com.money.api.repository.filter.LancamentoFilter;
import com.money.api.repository.projection.ResumoLancamento;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Lancamento salvar(Lancamento lancamento) {

		this.validacao(lancamento);

		return this.lancamentoRepository.save(lancamento);
	}

	private void validacao(Lancamento lancamento) {

		
		if (lancamento.getPessoa() == null || lancamento.getPessoa().getCodigo() == null) {
			throw new ValidationGenericException("mensagem.informar.pessoa");
		}

		if (lancamento.getCategoria() == null || lancamento.getCategoria().getCodigo() == null) {
			throw new ValidationGenericException("mensagem.informar.categoria");
		}

		Pessoa pessoa = this.pessoaRepository.findOne(lancamento.getPessoa().getCodigo());

		if (pessoa == null || pessoa.isInativo()) {
			throw new ValidationGenericException("mensagem.pessoa.invalida.ou.inativa");
		}

		Categoria categoria = this.categoriaRepository.findOne(lancamento.getCategoria().getCodigo());

		if (categoria == null) {
			throw new ValidationGenericException("mensagem.categoria.invalida");
		}
	}

	public Lancamento atualizar(Lancamento lancamento, Long codigo) {
		Lancamento lan = this.buscarPorId(codigo);
		
		// if (!lancamento.getPessoa().equals(lan.getPessoa())) {
		this.validacao(lancamento);
		// }

		BeanUtils.copyProperties(lancamento, lan, "codigo");
		return this.lancamentoRepository.save(lan);

	}

	public Lancamento buscarPorId(Long id) {
		return this.lancamentoRepository.findOne(id);
	}

	public Page<Lancamento> pesquisar(LancamentoFilter filter, Pageable pageable) {
		return this.lancamentoRepository.filtrar(filter, pageable);
	}

	public Page<ResumoLancamento> resumir(LancamentoFilter filter, Pageable pageable) {
		return this.lancamentoRepository.resumir(filter, pageable);
	}

	public void apagar(Long id) {
		this.lancamentoRepository.delete(id);
	}
}
