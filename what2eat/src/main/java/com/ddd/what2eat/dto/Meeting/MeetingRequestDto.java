package com.ddd.what2eat.dto.Meeting;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.ddd.what2eat.dto.Restaurant.RestaurantDto;
import com.ddd.what2eat.model.MeetingStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingRequestDto {
	@NotNull
	private LocalDateTime meetAt;

	private MeetingStatus status;

	@NotNull
	private Long userId;

	@NotNull
	private Integer memberLimit;

	private String content;

	@NotNull
	private RestaurantDto restaurant;

}
