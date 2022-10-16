package com.ddd.what2eat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

	USER_NOT_FOUND(400, "Wrong Id or Password"),
	DUPLICATE_USER_EMAIL(400, "This email is already registered."),
	PASSWORD_NOT_VALID(400, "The password rule is incorrect."),
	EMAIL_NOT_VALID(400, "The email rule is incorrect."),
	INVALID_EMAIL_TOKEN(400, "Invalid email token"),
	MEETING_NOT_FOUND(400, "Meeting Not Exist"),
	OWNER_NOT_AVAILABLE(400, "Owner Can't Update Attendance"),
	ALREADY_FULL(400, "Meeting Attendance is already full"),
	RESTAURANT_NOT_FOUND(400, "Restaurant Not Exist"),

	PASSWORD_NOT_CORRECT(401, "The password is incorrect"),
	UNAUTHORIZED(401, "Authentication error (cause: unauthorized)"),

	ALREADY_HAVE_SCHEDULE(400, "You already have scheudule in 2 hours"),
	USER_NOT_VERIFIED_EXCEPTION(401, "Not Verified");

	private int code;
	private String msg;
}
