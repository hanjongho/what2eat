package com.ddd.what2eat.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddd.what2eat.dto.PinResponseDto;
import com.ddd.what2eat.security.JwtAuthentication;
import com.ddd.what2eat.service.RestaurantService;

@RestController
public class PinController {

	private RestaurantService restaurantService;

	public PinController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@GetMapping("/pins")
	public List<PinResponseDto> retrievePinList(@AuthenticationPrincipal JwtAuthentication authentication) {
		return restaurantService.getValidRestaurantList()
			.stream()
			.map(PinResponseDto::new)
			.collect(Collectors.toList());
	}
}
