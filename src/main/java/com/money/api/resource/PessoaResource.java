package com.money.api.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.money.api.event.RecursoSalvoEvent;
import com.money.api.model.Pessoa;
import com.money.api.service.PessoaService;

@RestController
@RequestMapping("/pessoa")
public class PessoaResource {

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> salvar(@RequestBody @Valid Pessoa pessoa, HttpServletResponse response) {

		Pessoa pessoaSalva = this.pessoaService.adicionar(pessoa);

		publisher.publishEvent(new RecursoSalvoEvent(this, response, pessoaSalva.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}

/*	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<List<Pessoa>> buscarTodas() {

		List<Pessoa> pessoas = this.pessoaService.buscarTodas();

		return ResponseEntity.ok(pessoas);
	}
*/
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public Page<Pessoa> filtrarPessoas(String nome, Pageable pag) {

		return this.pessoaService.filtrarPessoa(nome, pag);

	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Pessoa> buscarPessoaPorId(@PathVariable Long codigo) {
		Pessoa pessoa = this.pessoaService.buscarPorId(codigo);

		return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	public void apagar(@PathVariable Long codigo) {
		this.pessoaService.apagar(codigo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<?> atualizar(@PathVariable Long codigo, @RequestBody @Valid Pessoa pessoa) {

		Pessoa pessoaAtualizada = this.pessoaService.atualizar(pessoa, codigo);

		return ResponseEntity.ok(pessoaAtualizada);
	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public void atualizarStatusAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		this.pessoaService.atualizarStatusAtivo(codigo, ativo);
	}
}
