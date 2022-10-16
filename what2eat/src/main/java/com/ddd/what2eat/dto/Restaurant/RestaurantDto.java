package com.ddd.what2eat.dto.Restaurant;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RestaurantDto {

	private String name;

	private String address;

	private Double longitude;

	private Double latitude;

	private String category;
}
