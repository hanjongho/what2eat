package com.ddd.what2eat.service;

import com.ddd.what2eat.dto.FindPasswordRequestDto;
import com.ddd.what2eat.utils.MailUtils;
import com.ddd.what2eat.dto.register.RegisterRequestDto;
import com.ddd.what2eat.exception.CustomException;
import com.ddd.what2eat.model.User;
import com.ddd.what2eat.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import javax.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Captor
	private ArgumentCaptor<User> userArgumentCaptor;

	@Mock
	private UserRepository mockUserRepository;

	@Mock
	private MailUtils mailUtils;

	@InjectMocks
	private UserService subject;

	@Test
	void 회원가입에_성공한다() throws MessagingException, UnsupportedEncodingException {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.email("test@test.com")
			.password("test1234@")
			.build();

		User user = User.builder()
			.build();

		when(mockUserRepository.save(userArgumentCaptor.capture())).thenReturn(user);

		subject.register(registerRequestDto);

		User value = userArgumentCaptor.getValue();
		assertEquals(registerRequestDto.getUserName(), value.getUserName());
		assertEquals(registerRequestDto.getEmail(), value.getEmail());
		assertEquals(registerRequestDto.getPassword(), value.getPassword());
	}

	@Test
	void 회원가입을_할_때_규칙에_맞지_않는_비밀번호를_작성하는_경우_에러를_반환한다() {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.email("test@test.com")
			.password("t")
			.build();

		assertThrows(CustomException.class, () ->
			subject.register(registerRequestDto));
	}

	@Test
	void 회원가입을_할_때_규칙에_맞지_않는_이메일을_작성할_경우_에러를_반환하다() {
		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.email("test")
			.password("test1234@")
			.build();

		assertThrows(CustomException.class, () ->
			subject.register(registerRequestDto));
	}

	@Test
	void 회원가입을_할_때_중복된_이메일을_작성할_경우_에러를_반환하다() {

		when(mockUserRepository.existsByEmail("test@test.com")).thenReturn(true);

		RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
			.userName("test")
			.email("test@test.com")
			.password("test1234@")
			.build();

		assertThrows(CustomException.class, () ->
			subject.register(registerRequestDto));
	}

	@Test
	void 비밀번호_찾기를_할_때_이메일을_정상적으로_작성하면_임시비밀번호를_준다() throws MessagingException, UnsupportedEncodingException {
		FindPasswordRequestDto findPasswordRequestDto = new FindPasswordRequestDto("test@test.com");
		User user = User.builder()
				.id(1L)
				.userName("test")
				.email("test@test.com")
				.emailVerified(true)
				.password("test1234!")
				.emailVerifiedToken("test-test-test")
				.build();
		Optional<User> optionalUser = Optional.of(user);
		when(mockUserRepository.findByEmail(findPasswordRequestDto.getEmail())).thenReturn(optionalUser);

		subject.makeTemporaryPassword(findPasswordRequestDto);
	}

	@Test
	void 비밀번호_찾기를_할때_이메일이_규칙에서_벗어나면_에러를_반환한다() {
		FindPasswordRequestDto findPasswordRequestDto = new FindPasswordRequestDto("test");

		assertThrows(CustomException.class, () ->
				subject.makeTemporaryPassword(findPasswordRequestDto));
	}

	@Test
	void 비밀번호_찾기를_할때_해당_이메일을_찾을_수_없는_경우_에러를_반환한다() {
		FindPasswordRequestDto findPasswordRequestDto = new FindPasswordRequestDto("test@test.com");

		assertThrows(CustomException.class, () ->
				subject.makeTemporaryPassword(findPasswordRequestDto));
	}
}