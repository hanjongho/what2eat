package com.ddd.what2eat.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddd.what2eat.dto.Attendance.AttendanceRequestDTO;
import com.ddd.what2eat.dto.Meeting.MeetingRequestDto;
import com.ddd.what2eat.dto.Meeting.MeetingResponseDto;
import com.ddd.what2eat.exception.CustomException;
import com.ddd.what2eat.exception.ExceptionCode;
import com.ddd.what2eat.model.Attendance;
import com.ddd.what2eat.model.Meeting;
import com.ddd.what2eat.model.MeetingStatus;
import com.ddd.what2eat.model.Restaurant;
import com.ddd.what2eat.model.User;
import com.ddd.what2eat.repository.AttendanceRepository;
import com.ddd.what2eat.repository.MeetingRepository;
import com.ddd.what2eat.repository.RestaurantRepository;
import com.ddd.what2eat.repository.UserRepository;

@Service
public class MeetingService {

	private final MeetingRepository meetingRepository;
	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;
	private final AttendanceRepository attendanceRepository;

	public MeetingService(MeetingRepository meetingRepository, RestaurantRepository restaurantRepository,
		UserRepository userRepository, AttendanceRepository attendanceRepository) {
		this.meetingRepository = meetingRepository;
		this.restaurantRepository = restaurantRepository;
		this.userRepository = userRepository;
		this.attendanceRepository = attendanceRepository;
	}

	@Transactional
	public MeetingResponseDto saveMeeting(MeetingRequestDto meetingRequestDto) {
		// 1. Dto -> Entity
		Meeting meeting = Meeting.of(meetingRequestDto);
		meeting.setStatus(MeetingStatus.WAITING);
		meeting.setId(null);

		// 2. 해당 가게 존재여부 확인
		Optional<Restaurant> restaurant = restaurantRepository.findByName(meetingRequestDto.getRestaurant().getName());
		if (restaurant.isPresent()) {
			meeting.setRestaurant(restaurant.get());
		}
		User user = userRepository.findById(meetingRequestDto.getUserId())
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
		user.setSchedule(meeting.getMeetAt());

		Attendance attendance = Attendance.builder()
			.user(user)
			.meeting(meeting)
			.build();
		attendance.setMeeting(meeting);
		attendanceRepository.save(attendance);
		// 3. Entity -> Dto
		Meeting save = meetingRepository.save(meeting);
		return MeetingResponseDto.of(save);
	}

	@Transactional
	public void updateMeeting(Long restaurantId, Long meetingId, MeetingRequestDto meetingRequestDto) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> new CustomException(ExceptionCode.MEETING_NOT_FOUND));
		meeting.setMeetAt(meetingRequestDto.getMeetAt());
		meeting.setMemberLimit(meetingRequestDto.getMemberLimit());
		meeting.setContent(meetingRequestDto.getContent());
		Optional<Restaurant> findRestaurant = restaurantRepository.findByName(
			meetingRequestDto.getRestaurant().getName());

		if (findRestaurant.isPresent()) {
			meeting.setRestaurant(findRestaurant.get());
		} else {
			Restaurant restaurant = Restaurant.builder()
				.name(meetingRequestDto.getRestaurant().getName())
				.address(meetingRequestDto.getRestaurant().getAddress())
				.longitude(meetingRequestDto.getRestaurant().getLongitude())
				.latitude(meetingRequestDto.getRestaurant().getLatitude())
				.build();
			meeting.setRestaurant(restaurant);
		}

		User user = userRepository.findById(meetingRequestDto.getUserId())
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
		user.setSchedule(meeting.getMeetAt());

	}

	public List<Meeting> getAllValidMeetingListOfPin(Long restaurantId) {
		return restaurantRepository.findById(restaurantId).orElseThrow(RuntimeException::new).getMeetingList()
			.stream()
			.filter(meeting -> meeting.getStatus() != MeetingStatus.EXPIRED)
			.collect(Collectors.toList());
	}

	@Transactional
	public List<Attendance> updateAttendance(Long meetingId, AttendanceRequestDTO attendanceRequestDTO) throws
		Exception {
		boolean attend = attendanceRequestDTO.getAttendance();
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> new CustomException(ExceptionCode.MEETING_NOT_FOUND));
		User user = userRepository.findById(attendanceRequestDTO.getUserId())
			.orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
		if (meeting.getUser().getId().equals(user.getId())) {
			throw new CustomException(ExceptionCode.OWNER_NOT_AVAILABLE);
		}

		if (attend) {
			if (meeting.getAttendanceList().size() == meeting.getMemberLimit()) {
				throw new CustomException(ExceptionCode.ALREADY_FULL);
			}

			// 처음 스케줄 만드는 거라서 column 값이 없는 경우
			if (user.getSchedule() == null) {
				user.setSchedule(meeting.getMeetAt());
			} else {
				LocalDateTime schedule = user.getSchedule();

				// 날짜
				if (schedule.toLocalDate().equals(meeting.getMeetAt().toLocalDate())) {
					// 약속 시간 기준 전후 2시간 내에 있으면 exception 던지기
					LocalTime scheduleTime = schedule.toLocalTime();
					LocalTime meetingTime = meeting.getMeetAt().toLocalTime();

					// 기존 약속 기준으로 전후 2시간 이내에 있으면 체크 불가
					if (Math.abs(ChronoUnit.HOURS.between(scheduleTime, meetingTime)) <= 2) {
						throw new CustomException(ExceptionCode.ALREADY_HAVE_SCHEDULE);
					} else {
						user.setSchedule(meeting.getMeetAt());
					}
					// 약속 날짜 다르면 업데이트 가능
				} else {
					user.setSchedule(meeting.getMeetAt());
				}
			}

			Attendance attendance = Attendance.builder()
				.user(user)
				.meeting(meeting)
				.build();
			attendance.setMeeting(meeting);
			attendanceRepository.save(attendance);

		} else {
			Attendance attendance = attendanceRepository.findByMeetingIdAndUserId(meeting.getId(), user.getId()).get();
			attendanceRepository.delete(attendance);
			user.setSchedule(null);
		}
		return meeting.getAttendanceList();

	}

	@Transactional
	public void deleteMeeting(Long meetingId) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> new CustomException(ExceptionCode.MEETING_NOT_FOUND));

		meetingRepository.delete(meeting);
	}
}
