package com.ddd.what2eat.controller;

import com.ddd.what2eat.dto.Attendance.AttendanceResponseDTO;
import com.ddd.what2eat.dto.Attendance.UserAttendanceDTO;
import com.ddd.what2eat.security.JwtAuthentication;
import com.ddd.what2eat.service.AttendanceService;
import com.ddd.what2eat.service.MeetingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/attendances/{userId}")
    public List<UserAttendanceDTO> getAttendancesByUserId(@PathVariable long userId, @AuthenticationPrincipal JwtAuthentication authentication) {

        return attendanceService.getAttendancesByUserId(userId);
    }
}
