package com.ddd.what2eat.dto.password;

import javax.validation.constraints.NotNull;

import lombok.Builder;

@Builder
public class PasswordRequestDto {
	@NotNull
	private String password;

	@NotNull
	private String newPassword;

	public String getPassword() {
		return password;
	}

	public String getNewPassword() {
		return newPassword;
	}
}
