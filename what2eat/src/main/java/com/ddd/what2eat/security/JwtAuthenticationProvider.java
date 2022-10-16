package com.ddd.what2eat.security;

import static org.springframework.security.core.authority.AuthorityUtils.*;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ddd.what2eat.model.Role;
import com.ddd.what2eat.model.User;
import com.ddd.what2eat.service.UserService;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final Jwt jwt;

	private final UserService userService;

	public JwtAuthenticationProvider(Jwt jwt, UserService userService) {
		this.jwt = jwt;
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken)authentication;
		return processUserAuthentication(authenticationToken.authenticationRequest());
	}

	private Authentication processUserAuthentication(AuthenticationRequest request) {
		try {
			User user = userService.login(request.getPrincipal(), request.getCredentials());
			JwtAuthenticationToken authenticated =
				new JwtAuthenticationToken(new JwtAuthentication(user.getId(), user.getUserName(), user.getEmail()),
					null, createAuthorityList(
					Role.USER.value()));
			String apiToken = user.newApiToken(jwt, new String[] {Role.USER.value()});
			authenticated.setDetails(new AuthenticationResult(apiToken, user.getId(), user.getUserName()));
			return authenticated;
		} catch (RuntimeException e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

}