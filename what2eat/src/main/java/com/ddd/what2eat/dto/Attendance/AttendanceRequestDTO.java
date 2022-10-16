package com.ddd.what2eat.dto.Attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRequestDTO {
	private Long userId;
	private Boolean attendance;

}
