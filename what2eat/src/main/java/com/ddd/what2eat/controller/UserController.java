package com.ddd.what2eat.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddd.what2eat.dto.FindPasswordRequestDto;
import com.ddd.what2eat.dto.password.PasswordRequestDto;
import com.ddd.what2eat.dto.register.RegisterRequestDto;
import com.ddd.what2eat.exception.CustomException;
import com.ddd.what2eat.exception.ExceptionCode;
import com.ddd.what2eat.repository.UserRepository;
import com.ddd.what2eat.security.AuthenticationRequest;
import com.ddd.what2eat.security.AuthenticationResult;
import com.ddd.what2eat.security.AuthenticationResultDto;
import com.ddd.what2eat.security.JwtAuthenticationToken;
import com.ddd.what2eat.service.UserService;

@RestController
public class UserController {

	private final UserService userService;
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;

	public UserController(UserService userService, UserRepository userRepository,
		AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/users/login")
	public AuthenticationResultDto authentication(@RequestBody AuthenticationRequest authRequest) {
		try {
			JwtAuthenticationToken authToken = new JwtAuthenticationToken(authRequest.getPrincipal(),
				authRequest.getCredentials());
			Authentication authentication = authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return new AuthenticationResultDto((AuthenticationResult)authentication.getDetails());
		} catch (AuthenticationException e) {
			throw new CustomException(ExceptionCode.UNAUTHORIZED);
		}
	}

	@PostMapping("/users/register")
	public ResponseEntity register(@Valid @RequestBody RegisterRequestDto registerRequestDto) throws
		MessagingException,
		UnsupportedEncodingException {
		userService.register(registerRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/users/token")
	public void checkEmailToken(@RequestParam Long id, @RequestParam String token, HttpServletResponse response) throws
		IOException {
		userService.checkEmailToken(id, token);
		String redirect_uri = "https://sdsfoodmate.co.kr/login";
		response.sendRedirect(redirect_uri);
	}

	@PostMapping("/users/find-password")
	public void findPassword(@Valid @RequestBody FindPasswordRequestDto findPasswordRequestDto) throws
		MessagingException,
		UnsupportedEncodingException {
		userService.makeTemporaryPassword(findPasswordRequestDto);
	}

	@PutMapping("/users/{userId}/password")
	public void updateMeeting(@PathVariable Long userId, @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
		userService.updateUser(userId, passwordRequestDto);
	}
}
