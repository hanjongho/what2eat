package com.ddd.what2eat.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ddd.what2eat.model.Restaurant;
import com.ddd.what2eat.service.RestaurantService;

@WebMvcTest(PinController.class)
class PinControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RestaurantService mockRestaurantService;

	@Test
	void 지도_상에_핀이_꽂혀_있을_때_서비스에_진입하면_지도에_존재하는_핀이_보인다() throws Exception {
		//given
		List<Restaurant> mockPinList = Arrays.asList(Restaurant.builder()
			.id(1L)
			.latitude(33.450705)
			.longitude(126.570677)
			.name("신전떡볶이")
			.build());
		when(mockRestaurantService.getValidRestaurantList()).thenReturn(mockPinList);

		String expectedResult = "[{\"restaurantId\":1,\"latitude\":33.450705,\"longitude\":126.570677,\"name\":\"신전떡볶이\"}]";
		mockMvc.perform(get("/pins"))
			.andExpect(content().string(is(expectedResult)))
			.andExpect(status().isOk());
	}
}