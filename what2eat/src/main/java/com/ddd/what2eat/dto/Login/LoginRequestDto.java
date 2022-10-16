package com.ddd.what2eat.dto.Login;

import javax.validation.constraints.NotNull;

import lombok.Builder;

@Builder
public class LoginRequestDto {
	@NotNull
	private String username;

	@NotNull
	private String password;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
