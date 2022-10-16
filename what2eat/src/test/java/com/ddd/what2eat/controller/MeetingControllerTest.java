package com.ddd.what2eat.controller;

import com.ddd.what2eat.dto.Attendance.AttendanceRequestDTO;
import com.ddd.what2eat.dto.Meeting.MeetingRequestDto;
import com.ddd.what2eat.dto.Meeting.MeetingResponseDto;
import com.ddd.what2eat.dto.Restaurant.RestaurantDto;
import com.ddd.what2eat.exception.CustomException;
import com.ddd.what2eat.exception.ExceptionCode;
import com.ddd.what2eat.model.Meeting;
import com.ddd.what2eat.model.MeetingStatus;
import com.ddd.what2eat.model.Restaurant;
import com.ddd.what2eat.service.AttendanceService;
import com.ddd.what2eat.service.MeetingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeetingController.class)
class MeetingControllerTest {

	public static final String USER_ID = "1";
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	MeetingService mockMeetingService;

	@MockBean
	AttendanceService mockAttendanceService;

	@Captor
	ArgumentCaptor<AttendanceRequestDTO> attendanceCaptor;

	@Test
	void 정상적으로_약속이_생성된다() throws Exception {
		//given
		MeetingRequestDto meetingRequestDto = MeetingRequestDto.builder()
			.userId(1L)
			.meetAt(LocalDateTime.now())
			.memberLimit(4)
			.content("궁금한 점은 010-1234-5678로 연락주세요.")
			.restaurant(RestaurantDto.builder()
				.name("엽기떡볶이 잠실점")
				.address("서울시 송파구 올림픽로 35길 124")
				.longitude(109.2321)
				.latitude(129.1231)
				.build())
			.build();
		String inputDto = objectMapper.writeValueAsString(meetingRequestDto);
		MeetingResponseDto meetingResponseDto = MeetingResponseDto.of(Meeting.of(meetingRequestDto));
		meetingResponseDto.setMeetingId(1L);
		when(mockMeetingService.saveMeeting(any())).thenReturn(meetingResponseDto);

		//then
		String outputDto = "{\"meetingId\":1}";
		mockMvc.perform(post("/meetings")
				.content(inputDto)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(content().string(is(outputDto)));
	}

	@Test
	void 약속_특정_정보가_누락된_경우_400에러가_반환된다() throws Exception {
		//given
		MeetingRequestDto meetingRequestDto = MeetingRequestDto.builder()
			.build();
		String inputDto = objectMapper.writeValueAsString(meetingRequestDto);

		//when
		when(mockMeetingService.saveMeeting(any())).thenThrow(new RuntimeException());

		//then

		mockMvc.perform(post("/meetings")
				.content(inputDto)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	public void Given특정핀을클릭한상태일때_When1말풍선컴포넌트를클릭하면_Then해당핀에소속된유효한약속리스트들을DB에서불러온다() throws Exception {
		//given
		Restaurant restaurant = Restaurant.builder()
			.id(1L)
			.name("엽기떡볶이 잠실점")
			.address("서울특별시 잠실동 잠실1로")
			.longitude(33.450705)
			.latitude(126.570677)
			.build();

		Meeting meeting1 = Meeting.builder()
			.id(1L)
			.status(MeetingStatus.WAITING)
			.memberLimit(4)
			.restaurant(restaurant)
			.build();

		when(mockMeetingService.getAllValidMeetingListOfPin(1L)).thenReturn(Arrays.asList(meeting1));

		// then
		String expect = "[{\"createdDate\":null,\"modifiedDate\":null,\"id\":1,\"meetAt\":null,\"status\":\"WAITING\",\"user\":null,\"memberLimit\":4,\"content\":null,\"restaurant\":{\"createdDate\":null,\"modifiedDate\":null,\"id\":1,\"name\":\"엽기떡볶이 잠실점\",\"address\":\"서울특별시 잠실동 잠실1로\",\"longitude\":33.450705,\"latitude\":126.570677},\"attendanceList\":null}]";

		// String expect = "[{\"id\":1,\"meetAt\":null,\"status\":\"WAITING\",\"user\":null,\"memberLimit\":4,\"content\":null,\"restaurant\":{\"id\":1,\"name\":\"엽기떡볶이 잠실점\",\"address\":\"서울특별시 잠실동 잠실1로\",\"longitude\":33.450705,\"latitude\":126.570677},\"attendanceList\":null}]";
		mockMvc.perform(get("/meetings/1"))
			.andExpect(status().isOk())
			.andExpect(content().string(is(expect)));
	}

	// TODO 추후
	@Test
	void 같은날_점심약속이_존재하는경우_400에러가_반환된다() throws Exception {
		//given

		//when

		//then
	}

	@Test
	void 사용자가_특정_약속에_체크하면_status_code와_변경된_데이터를_반환한다() throws Exception {
		// given
		String param = "{\n"
			+ "\t\t\"userId\": 1,\n"
			+ "\t\t\"attendance\": true\n"
			+ "}";

		when(mockMeetingService.updateAttendance(anyLong(), any())).thenReturn(Arrays.asList());

		mockMvc.perform(put("/meetings/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(param))
			.andExpect(status().isOk());
	}

	@Test()
	void 사용자가_특정_약속에_체크하면_만석인_경우_400에러가_반환된다() throws Exception {

		String param = "{\n"
			+ "\t\t\"userId\": 1,\n"
			+ "\t\t\"attendance\": true\n"
			+ "}";

		when(mockMeetingService.updateAttendance(anyLong(), any())).thenThrow(
			new CustomException(ExceptionCode.ALREADY_FULL));

		String expect = "{\"msg\":\"Meeting Attendance is already full\"}";
		mockMvc.perform(put("/meetings/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(param))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(expect));
	}
}