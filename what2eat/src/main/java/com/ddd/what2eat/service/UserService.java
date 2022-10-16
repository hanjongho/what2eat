package com.ddd.what2eat.service;

import static com.ddd.what2eat.utils.Utils.*;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.mail.MessagingException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddd.what2eat.dto.FindPasswordRequestDto;
import com.ddd.what2eat.dto.password.PasswordRequestDto;
import com.ddd.what2eat.dto.register.RegisterRequestDto;
import com.ddd.what2eat.exception.CustomException;
import com.ddd.what2eat.exception.ExceptionCode;
import com.ddd.what2eat.model.User;
import com.ddd.what2eat.repository.UserRepository;
import com.ddd.what2eat.utils.MailUtils;

@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final MailUtils mailUtils;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, MailUtils mailUtils, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.mailUtils = mailUtils;
		this.passwordEncoder = passwordEncoder;
	}

	public User login(String principal, String credentials) {
		User user = userRepository.findByEmail(principal)
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

		user.login(passwordEncoder, credentials);

		if (!(user.getEmailVerified())) {
			throw new CustomException(ExceptionCode.USER_NOT_VERIFIED_EXCEPTION);
		}

		return user;
	}

	@Transactional
	public void register(RegisterRequestDto registerRequestDto) throws
		MessagingException,
		UnsupportedEncodingException {
		// 1. 비밀번호 규칙 맞는지
		validatePassword(registerRequestDto.getPassword());

		// 2. 이메일 규칙 맞는지
		validateEmail(registerRequestDto.getEmail());

		// 3. 이메일 중복 회원인지
		duplicateMember(registerRequestDto.getEmail());

		// 4. 회원가입
		User user = User.builder()
			.userName(registerRequestDto.getUserName())
			.email(registerRequestDto.getEmail())
			.password(passwordEncoder.encode(registerRequestDto.getPassword()))
			.emailVerified(false)
			.schedule(null)
			.build();

		user.generateEmailVerifiedToken();

		User save = userRepository.save(user);

		mailUtils.sendVerified(save.getId(), save.getEmail(), save.getEmailVerifiedToken());
	}

	private void duplicateMember(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new CustomException(ExceptionCode.DUPLICATE_USER_EMAIL);
		}
	}

	@Transactional
	public void checkEmailToken(Long id, String token) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

		if (!user.getEmailVerifiedToken().equals(token)) {
			throw new CustomException(ExceptionCode.INVALID_EMAIL_TOKEN);
		}

		user.setEmailVerified(true);
	}

	@Transactional
	public void makeTemporaryPassword(FindPasswordRequestDto findPasswordRequestDto) throws
		MessagingException,
		UnsupportedEncodingException {
		String email = findPasswordRequestDto.getEmail();
		User user = userRepository.findByEmail(email).orElseThrow(
			() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

		String temporaryPassword = UUID.randomUUID().toString().substring(0, 8);
		user.setPassword(passwordEncoder.encode(temporaryPassword));

		mailUtils.sendPassword(email, temporaryPassword);
	}

	@Transactional
	public void updateUser(Long userId, PasswordRequestDto passwordRequestDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

		user.login(passwordEncoder, passwordRequestDto.getPassword());
		user.setPassword(passwordEncoder.encode(passwordRequestDto.getNewPassword()));
	}
}
