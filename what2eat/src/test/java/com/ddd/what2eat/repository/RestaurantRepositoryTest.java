package com.ddd.what2eat.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RestaurantRepositoryTest {

	// @Autowired
	// RestaurantRepository restaurantRepository;
	//
	// @Test
	// public void Given특정핀을클릭한상태일때_When1말풍선컴포넌트를클릭하면_Then해당핀에소속된유효한약속리스트들을DB에서불러온다() {
	//
	// 	List<Meeting> result = restaurantRepository.findById(2L)
	// 		.orElseThrow(RuntimeException::new)
	// 		.getMeetingList();
	// 	assertThat(result.size()).isEqualTo(1);
	// }
}
