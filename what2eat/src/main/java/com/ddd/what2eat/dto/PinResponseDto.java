package com.ddd.what2eat.dto;

import com.ddd.what2eat.model.Restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PinResponseDto {
	private Long restaurantId;
	private double latitude;
	private double longitude;
	private String name;

	private String category;

	public PinResponseDto(Restaurant restaurant) {
		this.restaurantId = restaurant.getId();
		this.latitude = restaurant.getLatitude();
		this.longitude = restaurant.getLongitude();
		this.category = restaurant.getCategory();
		this.name = restaurant.getName();
	}
}
