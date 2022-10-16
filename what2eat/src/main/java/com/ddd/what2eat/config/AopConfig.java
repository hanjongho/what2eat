package com.ddd.what2eat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.ddd.what2eat.aop.RequestLoggingAspect;
import com.ddd.what2eat.trace.LogTrace;
import com.ddd.what2eat.trace.ThreadFieldLogTrace;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

	@Bean
	public RequestLoggingAspect requestLoggingAspect() {
		return new RequestLoggingAspect();
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadFieldLogTrace();
	}
}
