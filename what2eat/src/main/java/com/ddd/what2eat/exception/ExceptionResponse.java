package com.ddd.what2eat.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {
	private String msg;

	public ExceptionResponse(ExceptionCode exceptionCode) {
		this.msg = exceptionCode.getMsg();
	}

	public ExceptionResponse(String msg) {
		this.msg = msg;
	}
}
