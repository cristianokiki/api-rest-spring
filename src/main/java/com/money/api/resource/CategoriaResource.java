package com.money.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.api.model.Categoria;
import com.money.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;

	/**
	 * Recebe requisição get na uri principal
	 * 
	 * @return lista com categoria
	 */
	// @CrossOrigin(maxAge = 10, origins = {"http://localhost:8000"})//controlar
	// origens aceita, caso nao definida aceita apenas da mesma origem
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar() {
		return this.categoriaRepository.findAll();
	}

	/**
	 * Recebe parametros da categoria e monta desserializa se estiver com dados
	 * iguais ao objeto, valida objeto annotados com Bean Validations, se passar na
	 * validação continua senao
	 * 
	 * @RequestBody faz a desserialização da Categoria
	 * @Valid faz a validação com o Bean Validation
	 * 
	 * @param categoria
	 * @param response
	 * @return
	 */
	@PostMapping
	// @ResponseStatus(HttpStatus.CREATED)//codigo de retorno
	// @RequestBody transforma os atributos em objeto
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> criar(@RequestBody @Valid Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = this.categoriaRepository.save(categoria); // response para adicionar dados no header

		// retorna o locale da uri e recurso salvo em formato em json
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}

	@GetMapping("/{codigo}") // path variavel
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Categoria> buscarPorCodigo(@PathVariable Long codigo) {
		Categoria categoria = this.categoriaRepository.findOne(codigo);

		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}

}
