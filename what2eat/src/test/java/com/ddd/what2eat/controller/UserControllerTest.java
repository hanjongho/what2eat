package com.ddd.what2eat.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ddd.what2eat.dto.FindPasswordRequestDto;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.ddd.what2eat.dto.Login.LoginResponseDto;
import com.ddd.what2eat.dto.register.RegisterRequestDto;
import com.ddd.what2eat.exception.CustomException;
import com.ddd.what2eat.exception.ExceptionCode;
import com.ddd.what2eat.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	UserService userService;

//	@Test
//	void 아이디_비밀번호가_입력되고_인증된_회원이_존재하면_유저_아이디와_이름이_반환된다() throws Exception {
//		//given
//		String input = "{\n"
//			+ "\"username\":\"testId\",\n"
//			+ "\"password\":\"12345678\"\n"
//			+ "}";
//		LoginResponseDto loginResponseDto = LoginResponseDto.builder()
//			.userName("testId")
//			.userId(1L)
//			.build();
//		when(userService.login(any(),anyString())).thenReturn(loginResponseDto);
//
//		//then
//		String expect = "{\"userId\":1,\"userName\":\"testId\"}";
//		mockMvc.perform(post("/users/login")
//				.content(input)
//				.contentType(MediaType.APPLICATION_JSON))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.userId").exists())
//			.andExpect(jsonPath("$.userName").exists())
//			.andExpect(content().string(is(expect)));
//	}

	@Test
	void 아이디_또는_비밀번호가_누락하면_InvalidFormatException이_반환된다() throws Exception {
		//given
		String input = "{\n"
			+ "\"username\":\"testId\"\n"
			+ "}";

		//then
		mockMvc.perform(post("/users/login")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	void 아이디_비밀번호가_입력되고_회원이_존재하지_않으면_UserNotFoundException이_반환된다() throws Exception {
		//given
		String input = "{\n"
			+ "\"username\":\"testId\",\n"
			+ "\"password\":\"12345678\"\n"
			+ "}";
		when(userService.login(any(),anyString())).thenThrow(new CustomException(ExceptionCode.USER_NOT_FOUND));

		//then
		String expect = "{\"msg\":\"Wrong Id or Password\"}";
		mockMvc.perform(post("/users/login")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.msg").exists())
			.andExpect(content().string(is(expect)));
	}

	@Test
	void 아이디_비밀번호가_입력되고_회원이_인증되지않으면_UserNotVerifiedException이_반환된다() throws Exception {
		//given
		String input = "{\n"
			+ "\"username\":\"testId\",\n"
			+ "\"password\":\"12345678\"\n"
			+ "}";
		when(userService.login(any(),anyString())).thenThrow(new CustomException(ExceptionCode.USER_NOT_VERIFIED_EXCEPTION));

		//then
		String expect = "{\"msg\":\"Not Verified\"}";
		mockMvc.perform(post("/users/login")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.msg").exists())
			.andExpect(content().string(is(expect)));

	}

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

		doThrow(new CustomException(ExceptionCode.DUPLICATE_USER_EMAIL)).when(userService).register(any());

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
				.content(objectMapper.writeValueAsString(registerRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
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

		doThrow(new CustomException(ExceptionCode.EMAIL_NOT_VALID)).when(userService).register(any());

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

		doThrow(new CustomException(ExceptionCode.PASSWORD_NOT_VALID)).when(userService).register(any());

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
				.content(objectMapper.writeValueAsString(registerRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(
				(e) -> assertTrue(e.getResolvedException().getClass().isAssignableFrom(CustomException.class))
			);
	}

	@Test
	void 비밀번호찾기할때_정상적으로입력한경우_임시비밀번호를제공한다() throws Exception {
		FindPasswordRequestDto findPasswordRequestDto = new FindPasswordRequestDto("test@test.com");

		mockMvc.perform(MockMvcRequestBuilders.post("/users/find-password")
						.content(objectMapper.writeValueAsString(findPasswordRequestDto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void 비밀번호찾기할때_이메일규칙에맞지않는경우_에러를반환한다() throws Exception {
		FindPasswordRequestDto findPasswordRequestDto = new FindPasswordRequestDto("test");

		doThrow(new CustomException(ExceptionCode.EMAIL_NOT_VALID))
				.when(userService).makeTemporaryPassword(any());

		mockMvc.perform(MockMvcRequestBuilders.post("/users/find-password")
						.content(objectMapper.writeValueAsString(findPasswordRequestDto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(
						(e) -> assertTrue(e.getResolvedException().getClass().isAssignableFrom(CustomException.class))
				);
	}

	@Test
	void 비밀번호찾기할때_해당계정이존재하지않는경우_에러를반환한다() throws Exception {
		FindPasswordRequestDto findPasswordRequestDto = new FindPasswordRequestDto("test@test.com");

		doThrow(new CustomException(ExceptionCode.USER_NOT_FOUND))
				.when(userService).makeTemporaryPassword(any());

		mockMvc.perform(MockMvcRequestBuilders.post("/users/find-password")
						.content(objectMapper.writeValueAsString(findPasswordRequestDto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(
						(e) -> assertTrue(e.getResolvedException().getClass().isAssignableFrom(CustomException.class))
				);
	}
}