package com.ddd.what2eat.service;

import com.ddd.what2eat.dto.Attendance.UserAttendanceDTO;
import com.ddd.what2eat.model.Attendance;
import com.ddd.what2eat.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {


    AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<UserAttendanceDTO> getAttendancesByUserId(Long userId) {
        List<Attendance> attendances = attendanceRepository.findAllByUserId(userId);

        // TODO: 효율성 안좋은거 같아보임.. 자문 필요
        // TODO: 현재 시간보다 뒤에 꺼만 보여주도록 했는데 해당 로직이 적절한지 자문 필요
        return attendances.stream()
                .map(attendance -> UserAttendanceDTO.builder()
                    .restaurantName(attendance.getMeeting().getRestaurant().getName())
                    .meetAt(attendance.getMeeting().getMeetAt())
                    .longitude(attendance.getMeeting().getRestaurant().getLongitude())
                    .latitude(attendance.getMeeting().getRestaurant().getLatitude())
                    .build())
                .filter(item->{
                    LocalDateTime itemDateTime=item.getMeetAt();
                    LocalDateTime currentTime=LocalDateTime.now();
                    return itemDateTime.isAfter(currentTime);
                })
                .sorted(Comparator.comparing(UserAttendanceDTO::getMeetAt))
                .collect(Collectors.toList());
    }
}
