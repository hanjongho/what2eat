package com.ddd.what2eat.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.ddd.what2eat.dto.Meeting.MeetingRequestDto;
import com.ddd.what2eat.utils.ModelMapperUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "restaurant")
public class Meeting extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime meetAt;

	@Enumerated(EnumType.ORDINAL)
	private MeetingStatus status;

	@ManyToOne
	private User user;

	private Integer memberLimit;

	private String content;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Restaurant restaurant;

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.REMOVE)
	private List<Attendance> attendanceList = new ArrayList<>();

	public static Meeting of(MeetingRequestDto meetingRequestDto) {
		return ModelMapperUtils.getModelMapper().map(meetingRequestDto, Meeting.class);
	}
}
