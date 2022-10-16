package com.ddd.what2eat.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ddd.what2eat.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private RestaurantService restaurantService;

	//
	// @Test
	// void 활성화된_약속이_존재하는_식당_리스트를_반환한다() {
	// 	// given
	// 	Restaurant sinjun = new Restaurant(1L, "신전떡볶이", "서울 어딘가", 126.570677, 33.450705,
	// 		new ArrayList<>());
	// 	Restaurant yupki = new Restaurant(2L, "엽기떡볶이", "서울 어딘가2", 126.570738, 33.451393,
	// 		new ArrayList<>());
	// 	Restaurant chicken = new Restaurant(3L, "치킨집", "서울 어딘가3", 126.570738, 33.451393,
	// 		new ArrayList<>());
	//
	// 	Meeting one = new Meeting(1L, LocalDateTime.now(), MeetingStatus.WAITING, null, 2, "one", sinjun,
	// 		new ArrayList<>());
	// 	Meeting two = new Meeting(2L, LocalDateTime.now(), MeetingStatus.WAITING, null, 2, "two", yupki,
	// 		new ArrayList<>());
	// 	Meeting three = new Meeting(3L, LocalDateTime.now(), MeetingStatus.EXPIRED, null, 2, "three", chicken,
	// 		new ArrayList<>());
	// 	sinjun.addMeeting(one);
	// 	yupki.addMeeting(two);
	// 	chicken.addMeeting(three);
	// 	when(restaurantRepository.findAll()).thenReturn(Arrays.asList(sinjun, yupki));
	//
	// 	// when
	// 	List<Restaurant> validRestaurantList = restaurantService.getValidRestaurantList();
	//
	// 	// then
	// 	assertEquals(validRestaurantList.size(), 2);
	// }
}