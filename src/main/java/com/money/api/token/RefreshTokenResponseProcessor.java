package com.money.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.money.api.config.property.MoneyApiProperty;

@ControllerAdvice
public class RefreshTokenResponseProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

	@Autowired
	private MoneyApiProperty moneyApiProperty;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {

		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();

		String refreshToken = body.getRefreshToken().getValue();

		DefaultOAuth2AccessToken corpo = (DefaultOAuth2AccessToken) body;

		adicionarRefreshTokenEmCookie(refreshToken, req, resp);
		removerRefreshTokenDoBody(corpo);
 
		return body;
	}

	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken corpo) { //
		corpo.setRefreshToken(null);
	}

	private void adicionarRefreshTokenEmCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(this.moneyApiProperty.getSeguranca().isEnableHttps());// true em produção
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(2592000);
		resp.addCookie(cookie);

	}

}
