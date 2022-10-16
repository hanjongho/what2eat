package com.ddd.what2eat.utils;

import java.util.regex.Pattern;

import com.ddd.what2eat.exception.CustomException;
import com.ddd.what2eat.exception.ExceptionCode;

public class Utils {
	public static void validatePassword(String password) {
		Pattern pattern = Pattern.compile("^.*(?=^.{8,16}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$");

		if (!pattern.matcher(password).find()) {
			throw new CustomException(ExceptionCode.PASSWORD_NOT_VALID);
		}
	}

	public static void validateEmail(String email) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");

		if (!pattern.matcher(email).find()) {
			throw new CustomException(ExceptionCode.EMAIL_NOT_VALID);
		}
	}

}
