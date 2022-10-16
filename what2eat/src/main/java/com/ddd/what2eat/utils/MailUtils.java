package com.ddd.what2eat.utils;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailUtils {
	@Value("${address}")
	private String FROM_ADDRESS;

	private final JavaMailSender mailSender;

	public MailUtils(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Async
	public void sendVerified(Long id, String email, String token) throws
		MessagingException,
		UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
		mimeMessageHelper.setFrom(FROM_ADDRESS, FROM_ADDRESS);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject("[뭐드실] 인증 이메일 입니다");
		mimeMessageHelper.setText("https://sdsfoodmate.co.kr/api/users/token?id=" + id + "&token=" + token);

		mailSender.send(message);
	}

	@Async
	public void sendPassword(String email, String temporaryPassword) throws
		MessagingException,
		UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
		mimeMessageHelper.setFrom(FROM_ADDRESS, FROM_ADDRESS);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject("[뭐드실] 임시 비밀번호 입니다.");
		mimeMessageHelper.setText(temporaryPassword, true);

		mailSender.send(message);
	}
}
