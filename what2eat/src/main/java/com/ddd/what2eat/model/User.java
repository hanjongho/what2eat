package com.ddd.what2eat.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ddd.what2eat.exception.CustomException;
import com.ddd.what2eat.exception.ExceptionCode;
import com.ddd.what2eat.security.Jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@Table(name = "Users")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userName;

	private String password;

	private String email;

	private String emailVerifiedToken;

	private Boolean emailVerified;

	private LocalDateTime schedule;

	public void generateEmailVerifiedToken() {
		this.emailVerifiedToken = UUID.randomUUID().toString();
	}

	public boolean isValidToken(String token) {
		return this.emailVerifiedToken.equals(token);
	}

	public void login(PasswordEncoder passwordEncoder, String credentials) {
		if (!passwordEncoder.matches(credentials, password))
			throw new CustomException(ExceptionCode.USER_NOT_FOUND);
	}

	public String newApiToken(Jwt jwt, String[] roles) {
		Jwt.Claims claims = Jwt.Claims.of(id, userName, email, roles);
		return jwt.newToken(claims);
	}
}
