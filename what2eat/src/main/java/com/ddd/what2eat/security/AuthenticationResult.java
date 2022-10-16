package com.ddd.what2eat.security;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthenticationResult {

	private final String apiToken;
	private final Long userId;
	private final String userName;

	public AuthenticationResult(String apiToken, Long userId, String userName) {
		this.apiToken = apiToken;
		this.userId = userId;
		this.userName = userName;
	}

	public String getApiToken() {
		return apiToken;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
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