package com.ddd.what2eat.dto.Login;

import com.ddd.what2eat.model.User;
import com.ddd.what2eat.utils.ModelMapperUtils;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {

	private Long userId;

	private String userName;

	// TODO ModelMapper 에러 처리 해야됨
	public static LoginResponseDto of(User user) {
		return ModelMapperUtils.getModelMapper().map(user, LoginResponseDto.class);
	}

	public LoginResponseDto(Long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}
}
