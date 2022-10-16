package com.ddd.what2eat.security;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ddd.what2eat.exception.CustomException;
import com.ddd.what2eat.exception.ExceptionCode;

public class JwtAuthentication {

	public final Long id;

	public final String name;

	public final String email;

	JwtAuthentication(Long id, String name, String email) {
		if (id == null) {
			throw new CustomException(ExceptionCode.UNAUTHORIZED);
		}
		this.id = id;
		this.name = name;
		this.email = email;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", id)
			.append("email", email)
			.toString();
	}

}