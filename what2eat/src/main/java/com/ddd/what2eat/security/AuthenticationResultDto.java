package com.ddd.what2eat.security;

import static org.springframework.beans.BeanUtils.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthenticationResultDto {

	private String apiToken;
	private Long userId;
	private String userName;

	public AuthenticationResultDto(AuthenticationResult source) {
		copyProperties(source, this);
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("apiToken", apiToken)
			.append("userId", userId)
			.append("userName", userName)
			.toString();
	}

}