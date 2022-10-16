package com.ddd.what2eat.exception;

import lombok.Getter;

@Getter
public class ApiError {

	private final String msg;

	ApiError(String message) {
		this.msg = message;
	}

}