package com.ddd.what2eat.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.ddd.what2eat.dto.register.RegisterRequestDto;
import com.ddd.what2eat.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class UserControllerAPITest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void 회원가입한다() throws Exception {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.email("test@test.com")
			.password("test1234@")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
				.content(objectMapper.writeValueAsString(registerRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
	}

	@Test
	void 이미_가입된_회원의_이메일일_경우_회원가입_되지_않는다() throws Exception {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.email("test@test.com")
			.password("test1234@")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
			.content(objectMapper.writeValueAsString(registerRequestDto))
			.contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
				.content(objectMapper.writeValueAsString(registerRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(// assert로 예외를 검사하는 람다 사용
				(e) -> assertTrue(e.getResolvedException().getClass().isAssignableFrom(CustomException.class))
			);
	}

	@Test
	void 회원가입할때_이름이_없을경우_에러를_반환한다() throws Exception {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.email("test@test.com")
			.password("test1234@")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
				.content(objectMapper.writeValueAsString(registerRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(
				(e) -> assertTrue(
					e.getResolvedException().getClass().isAssignableFrom(MethodArgumentNotValidException.class))
			);
	}

	@Test
	void 회원가입할때_이메일이_없을경우_에러를_반환한다() throws Exception {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.password("test1234@")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
				.content(objectMapper.writeValueAsString(registerRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(
				(e) -> assertTrue(
					e.getResolvedException().getClass().isAssignableFrom(MethodArgumentNotValidException.class))
			);

	}

	@Test
	void 회원가입할때_비밀번호가_없을경우_에러를_반환한다() throws Exception {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.email("test@test.com")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
				.content(objectMapper.writeValueAsString(registerRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(
				(e) -> assertTrue(
					e.getResolvedException().getClass().isAssignableFrom(MethodArgumentNotValidException.class))
			);

	}

	@Test
	void 회원가입할때_이메일규칙이_틀렸을경우_에러를_반환한다() throws Exception {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.email("testtest.com")
			.password("test1234@")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
				.content(objectMapper.writeValueAsString(registerRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(
				(e) -> assertTrue(e.getResolvedException().getClass().isAssignableFrom(CustomException.class))
			);
	}

	@Test
	void 회원가입할때_비밀번호규칙이_틀렸을경우_에러를_반환한다() throws Exception {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.email("test@test.com")
			.password("t")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
				.content(objectMapper.writeValueAsString(registerRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(
				(e) -> assertTrue(e.getResolvedException().getClass().isAssignableFrom(CustomException.class))
			);
	}

}