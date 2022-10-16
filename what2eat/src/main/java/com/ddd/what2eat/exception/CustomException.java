package com.ddd.what2eat.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private ExceptionCode exceptionCode;

	public CustomException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
