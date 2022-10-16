package com.ddd.what2eat;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ddd.what2eat.config.JwtTokenConfig;
import com.ddd.what2eat.security.Jwt;

@SpringBootApplication
@EnableAsync
public class What2eatApplication {

	public static void main(String[] args) {
		SpringApplication.run(What2eatApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.initialize();
		return executor;
	}

	@Bean
	public Jwt jwt(JwtTokenConfig jwtTokenConfig) {
		return new Jwt(jwtTokenConfig.getIssuer(), jwtTokenConfig.getClientSecret(), jwtTokenConfig.getExpirySeconds());
	}

}
