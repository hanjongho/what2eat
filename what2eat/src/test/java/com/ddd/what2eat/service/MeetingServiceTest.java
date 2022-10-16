package com.ddd.what2eat.service;

import com.ddd.what2eat.dto.Attendance.AttendanceRequestDTO;
import com.ddd.what2eat.dto.Meeting.MeetingRequestDto;
import com.ddd.what2eat.dto.Meeting.MeetingResponseDto;
import com.ddd.what2eat.dto.Restaurant.RestaurantDto;
import com.ddd.what2eat.model.*;
import com.ddd.what2eat.repository.AttendanceRepository;
import com.ddd.what2eat.repository.MeetingRepository;
import com.ddd.what2eat.repository.RestaurantRepository;
import com.ddd.what2eat.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

	@InjectMocks
	MeetingService mockMeetingService;

	@Mock
	MeetingRepository mockMeetingRepository;

	@Mock
	RestaurantRepository mockRestaurantRepository;

	@Mock
	AttendanceRepository mockAttendanceRepository;

	@Mock
	UserRepository mockUserRepository;

	@Test
	void 정상적인_약속이면_생성하기() throws Exception {
		//given
		MeetingRequestDto meetingRequestDto = MeetingRequestDto.builder()
			.meetAt(LocalDateTime.now())
			.userId(1L)
			.memberLimit(4)
			.content("궁금한 점은 010-1234-5678로 연락주세요.")
			.restaurant(RestaurantDto.builder()
				.name("엽기떡볶이 잠실점")
				.address("서울시 송파구 올림픽로 35길 124")
				.longitude(109.2321)
				.latitude(129.1231)
				.build())
			.build();

		Meeting meeting = Meeting.of(meetingRequestDto);
		meeting.setId(1L);
		when(mockUserRepository.findById(1L)).thenReturn(Optional.of(new User()));
		when(mockMeetingRepository.save(any())).thenReturn(meeting);
		when(mockAttendanceRepository.save(any())).thenReturn(new Attendance());

		//when
		MeetingResponseDto meetingResponseDto = mockMeetingService.saveMeeting(meetingRequestDto);

		//then TODO
		// verify(mockMeetingRepository, times(1)).save(meeting);

		assertThat(meetingResponseDto.getMeetingId(), is(1L));
	}

	@Captor
	private ArgumentCaptor<Meeting> meetingArgumentCaptor;

	@Test
	void 식당이_존재하면_해당_식당의_ID가_반환된다() throws Exception {
		//given
		MeetingRequestDto meetingRequestDto = MeetingRequestDto.builder()
			.meetAt(LocalDateTime.now())
			.userId(1L)
			.memberLimit(4)
			.content("궁금한 점은 010-1234-5678로 연락주세요.")
			.restaurant(RestaurantDto.builder()
				.name("엽기떡볶이 잠실점")
				.address("서울시 송파구 올림픽로 35길 124")
				.longitude(109.2321)
				.latitude(129.1231)
				.build())
			.build();

		Meeting meeting = Meeting.of(meetingRequestDto);
		meeting.setId(1L);

		when(mockMeetingRepository.save(meetingArgumentCaptor.capture())).thenReturn(meeting);

		Restaurant restaurant = Restaurant.builder()
			.id(2L)
			.name("엽기떡볶이 잠실점")
			.build();

		when(mockRestaurantRepository.findByName("엽기떡볶이 잠실점")).thenReturn(Optional.of(restaurant));
		when(mockUserRepository.findById(any())).thenReturn(Optional.of(new User()));

		//when
		mockMeetingService.saveMeeting(meetingRequestDto);

		//then
		assertThat(meetingArgumentCaptor.getValue().getRestaurant().getId(), is(2L));
	}

	@Test
	void Given특정핀을클릭한상태일때_When1말풍선컴포넌트를클릭하면_Then해당핀에소속된유효한약속리스트들을DB에서불러온다() throws Exception {
		//given
		User user = User.builder()
			.id(1L)
			.userName("yoshi")
			.password("12345678")
			.email("test@gmail.com")
			.emailVerified(true)
			.build();

		Restaurant restaurant = Restaurant.builder()
			.id(1L)
			.name("엽기떡볶이 잠실점")
			.address("서울특별시 잠실동 잠실1로")
			.longitude(33.450705)
			.latitude(126.570677)
			.build();

		Meeting meeting1 = Meeting.builder()
			.id(1L)
			.user(user)
			.status(MeetingStatus.WAITING)
			.memberLimit(4)
			.restaurant(restaurant)
			.build();

		Meeting meeting2 = Meeting.builder()
			.id(2L)
			.status(MeetingStatus.EXPIRED)
			.memberLimit(2)
			.restaurant(restaurant)
			.build();

		restaurant.addMeeting(meeting1);
		restaurant.addMeeting(meeting2);

		when(mockRestaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

		//when

		List<Meeting> allValidMeetingListOfPin = mockMeetingService.getAllValidMeetingListOfPin(1L);

		//then
		assertThat(allValidMeetingListOfPin.size(), is(1));
		assertThat(allValidMeetingListOfPin.get(0).getId(), is(1L));
		assertThat(allValidMeetingListOfPin.get(0).getStatus(), is(MeetingStatus.WAITING));
		assertThat(allValidMeetingListOfPin.get(0).getMemberLimit(), is(4));
	}

	@Test
	void 이미_점심_약속이_존재하는_경우_400에러를_반환한다() throws Exception {
		//given

		//when

		//then

	}

	@Test
	void 사용자가_특정_약속에_체크하면_약속참석자에_추가된다() throws Exception {
		User user = User.builder()
			.id(1L)
			.userName("yoshi")
			.password("12345678")
			.email("test@gmail.com")
			.emailVerified(true)
			.build();

		User user2 = User.builder()
			.id(2L)
			.userName("bart")
			.password("123")
			.email("rla@gmail.com")
			.emailVerified(true)
			.build();

		Restaurant restaurant = Restaurant.builder()
			.id(1L)
			.name("엽기떡볶이 잠실점")
			.address("서울특별시 잠실동 잠실1로")
			.longitude(33.450705)
			.latitude(126.570677)
			.build();

		Meeting meeting1 = Meeting.builder()
			.id(1L)
			.user(user)
			.status(MeetingStatus.WAITING)
			.memberLimit(4)
			.restaurant(restaurant)
			.attendanceList(Arrays.asList())
			.build();

		Attendance attendance = Attendance.builder()
			.id(1L)
			.meeting(meeting1)
			.user(user2)
			.build();

		AttendanceRequestDTO attendanceRequestDTO = AttendanceRequestDTO.builder()
			.userId(2L)
			.attendance(true)
			.build();

	}
}