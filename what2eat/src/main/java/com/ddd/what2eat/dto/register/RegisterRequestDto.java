package com.ddd.what2eat.dto.register;

import javax.validation.constraints.NotNull;

import lombok.Builder;

@Builder
public class RegisterRequestDto {
	@NotNull
	private String userName;

	@NotNull
	private String password;

	@NotNull
	private String email;

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
}
