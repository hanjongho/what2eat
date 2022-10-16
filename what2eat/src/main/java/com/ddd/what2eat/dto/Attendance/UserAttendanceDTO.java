package com.ddd.what2eat.dto.Attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAttendanceDTO {
    private LocalDateTime meetAt;
    private String restaurantName;
    private Double latitude;
    private Double longitude;
}
