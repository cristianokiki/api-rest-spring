package com.money.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenRequestProcessor implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		if ("/oauth/token".equalsIgnoreCase(req.getRequestURI())
				&& "refresh_token".equalsIgnoreCase(req.getParameter("grant_type")) && req.getCookies() != null) {

			for (Cookie cookie : req.getCookies()) {
				if (cookie.getName().equalsIgnoreCase("refreshToken")) {
					req = new NovaRequisicao(req, cookie.getValue());
				}
			}

		}

		chain.doFilter(req, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}

class NovaRequisicao extends HttpServletRequestWrapper {

	private String refreshToken;

	public NovaRequisicao(HttpServletRequest request, String refreshToken) {
		super(request);
		this.refreshToken = refreshToken;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		ParameterMap<String, String[]> map = new ParameterMap<>(super.getRequest().getParameterMap());
		map.put("refresh_token", new String[] { this.refreshToken });
		map.setLocked(true);
		return map;
	}

}
