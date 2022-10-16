package com.ddd.what2eat.security;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;

	private String credentials;

	public JwtAuthenticationToken(String principal, String credentials) {
		super(null);
		super.setAuthenticated(false);

		this.principal = principal;
		this.credentials = credentials;
	}

	JwtAuthenticationToken(Object principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		super.setAuthenticated(true);

		this.principal = principal;
		this.credentials = credentials;
	}

	AuthenticationRequest authenticationRequest() {
		return new AuthenticationRequest(String.valueOf(principal), credentials);
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public String getCredentials() {
		return credentials;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException("Cannot set this token to trusted");
		}
		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		credentials = null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("principal", principal)
			.append("credentials", "[PROTECTED]")
			.toString();
	}

}