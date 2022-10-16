package com.ddd.what2eat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ddd.what2eat.model.MeetingStatus;
import com.ddd.what2eat.model.Restaurant;
import com.ddd.what2eat.repository.RestaurantRepository;

@Service
public class RestaurantService {

	private RestaurantRepository restaurantRepository;

	public RestaurantService(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	public List<Restaurant> getValidRestaurantList() {
		return restaurantRepository.findAll()
			.stream()
			.filter(e -> e.getMeetingList().stream().anyMatch(m -> m.getStatus() != MeetingStatus.EXPIRED))
			.collect(Collectors.toList());
	}
}
