package com.ddd.what2eat.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ddd.what2eat.dto.Attendance.AttendanceRequestDTO;
import com.ddd.what2eat.dto.Meeting.MeetingRequestDto;
import com.ddd.what2eat.dto.Meeting.MeetingResponseDto;
import com.ddd.what2eat.model.Attendance;
import com.ddd.what2eat.model.Meeting;
import com.ddd.what2eat.security.JwtAuthentication;
import com.ddd.what2eat.service.MeetingService;

@RestController
public class MeetingController {

	private final MeetingService meetingService;

	public MeetingController(MeetingService meetingService) {
		this.meetingService = meetingService;
	}

	@GetMapping("/meetings/{restaurantId}")
	public List<Meeting> getAllValidMeetingsByRestaurantId(
		@PathVariable long restaurantId,
		@AuthenticationPrincipal JwtAuthentication authentication) {
		return meetingService.getAllValidMeetingListOfPin(restaurantId);
	}

	@PutMapping("/meetings/{meetingId}")
	public List<Attendance> updateAttendance(
		@PathVariable Long meetingId,
		@RequestBody AttendanceRequestDTO attendanceDTO,
		@AuthenticationPrincipal JwtAuthentication authentication) throws Exception {
		// TODO: insert code
		return meetingService.updateAttendance(meetingId, attendanceDTO);
	}

	@PutMapping("/meetings/{restaurantId}/{meetingId}")
	public void updateMeeting(
		@PathVariable Long restaurantId,
		@PathVariable Long meetingId,
		@Valid @RequestBody MeetingRequestDto meetingRequestDto,
		@AuthenticationPrincipal JwtAuthentication authentication) {
		meetingService.updateMeeting(restaurantId, meetingId, meetingRequestDto);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/meetings")
	public MeetingResponseDto saveMeeting(
		@Valid @RequestBody MeetingRequestDto meetingRequestDto,
		@AuthenticationPrincipal JwtAuthentication authentication) {
		return meetingService.saveMeeting(meetingRequestDto);
	}

	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("meetings/{meetingId}")
	public void deleteMeeting(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal JwtAuthentication authentication) {
		meetingService.deleteMeeting(meetingId);
	}

}
