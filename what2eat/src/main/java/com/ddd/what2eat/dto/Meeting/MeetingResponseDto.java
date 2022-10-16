package com.ddd.what2eat.dto.Meeting;

import com.ddd.what2eat.model.Meeting;
import com.ddd.what2eat.utils.ModelMapperUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingResponseDto {
	private Long meetingId;

	public static MeetingResponseDto of(Meeting meeting) {
		return ModelMapperUtils.getModelMapper().map(meeting, MeetingResponseDto.class);
	}
}
