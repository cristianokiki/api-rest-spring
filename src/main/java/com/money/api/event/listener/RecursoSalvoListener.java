package com.money.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.money.api.event.RecursoSalvoEvent;

@Component
public class RecursoSalvoListener implements ApplicationListener<RecursoSalvoEvent> {

	@Override
	public void onApplicationEvent(RecursoSalvoEvent event) {
		
		HttpServletResponse response = event.getResponse();
		Long codigo = event.getCodigo();
		
		adicionaHeaderLocation(response, codigo);
		
	}

	/**
	 * Retorna endereço do recurso criado apartir da requisição atual
	 * 
	 * @param response
	 * @param codigo
	 */
	private void adicionaHeaderLocation(HttpServletResponse response, Long codigo) {
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
		.buildAndExpand(codigo).toUri();
		
		response.setHeader("Location", uri.toASCIIString());
	}

}
