package com.ddd.what2eat.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

	private final ObjectMapper om;

	public EntryPointUnauthorizedHandler(ObjectMapper om) {
		this.om = om;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException,
		ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("content-type", "application/json");
		response.getWriter().write(om.writeValueAsString("2"));
		response.getWriter().flush();
		response.getWriter().close();
	}

}