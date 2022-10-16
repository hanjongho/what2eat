package com.ddd.what2eat.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.ddd.what2eat.dto.Restaurant.RestaurantDto;
import com.ddd.what2eat.utils.ModelMapperUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String address;

	private Double longitude;

	private Double latitude;

	private String category;

	@JsonIgnore
	@OneToMany(mappedBy = "restaurant")
	@Builder.Default
	private List<Meeting> meetingList = new ArrayList<Meeting>();

	public static Restaurant of(RestaurantDto restaurantDto) {
		return ModelMapperUtils.getModelMapper().map(restaurantDto, Restaurant.class);
	}

	public void addMeeting(Meeting meeting) {
		meetingList.add(meeting);
	}
}
