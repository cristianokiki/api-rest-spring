package com.money.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.money.api.util.Erro;

/**
 * Annotation que indica que vale para toda a aplicação
 * 
 * @author cristiano
 *
 */
@ControllerAdvice
public class GenericResponseExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Injeta objeto que faz leitura do arquivo messages.properties
	 */
	@Autowired
	private MessageSource messageSource;

	/**
	 * Lidar com mensagem não lida na requisição http, exceção lançada quando recebe
	 * um campo que não pertence ao objeto desserializado
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return super.handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * Captura erros do Bean Validation trata e retorna no corpo da requisição.
	 * Lidar com argumentos de métodos não validados com a annotation @Valid do Bean
	 * Validation
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Erro> erros = criarListaErro(ex.getBindingResult());

		return super.handleExceptionInternal(ex, erros, headers, status, request);
	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<?> handleEmptyResultDataAccessException(RuntimeException ex, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return super.handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<?> handleDataIntegrityViolationException(RuntimeException ex, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("recurso.nao-permitido", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return super.handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * Recebe uma objeto BindingResult que contem a lista de erros de campos não
	 * validados e converte em uma lista de erros para usuário e desenvolvedor
	 * 
	 * @param bindingResult
	 * @return lista de erros
	 */
	public List<Erro> criarListaErro(BindingResult bindingResult) {
		List<Erro> erros = new ArrayList<Erro>();

		for (FieldError erro : bindingResult.getFieldErrors()) {

			String mensagemUsuario = messageSource.getMessage(erro, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = erro.toString();

			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}

		return erros;
	}

}
