package com.ddd.what2eat.aop;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.ddd.what2eat.security.Jwt.Claims;
import com.ddd.what2eat.trace.LogTrace;
import com.ddd.what2eat.trace.TraceStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class RequestLoggingAspect {

	@Autowired
	private LogTrace trace;

	@Pointcut("execution(* com.ddd.what2eat..*(..))")
	private void allWhat2eat() {
	}

	@Pointcut("execution(* *..*Controller.*(..))")
	private void allController() {
	}

	@Pointcut("execution(* *..MeetingController.*(..))")
	private void meetingController() {
	}

	@Pointcut("execution(* *..PinController.*(..))")
	private void pinController() {
	}

	@Pointcut("execution(* *..*Service.*(..))")
	private void allService() {
	}

	@Pointcut("execution(* *..*Repository.*(..))")
	private void allRepository() {
	}

	@Around("meetingController() || pinController()")
	public Object doControllerLog(ProceedingJoinPoint joinPoint) throws Throwable {

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

		String jwtToken = obtainAuthorizationToken(request.getHeader("api_key"));
		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512("Rel3Bjce2MajBo09qgkNgYaTuzvJe8iwnBFhsDS5"))
			.withIssuer("social_server")
			.build();
		Claims claims = new Claims(jwtVerifier.verify(jwtToken));

		TraceStatus status1 = trace.begin(joinPoint.getSignature().toString(), claims.userKey);
		Object proceed = joinPoint.proceed();
		trace.end(status1);
		return proceed;
	}

	@Around("allService() || allRepository()")
	public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {

		TraceStatus status1 = trace.begin(joinPoint.getSignature().toString(), -1L);
		Object proceed = joinPoint.proceed();
		trace.end(status1);
		return proceed;
	}

	private String obtainAuthorizationToken(String token) {

		final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

		if (token != null) {
			if (log.isDebugEnabled())
				log.debug("Jwt authorization api detected: {}", token);
			try {
				token = URLDecoder.decode(token, "UTF-8");
				String[] parts = token.split(" ");
				if (parts.length == 2) {
					String scheme = parts[0];
					String credentials = parts[1];
					return BEARER.matcher(scheme).matches() ? credentials : null;
				}
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
		}

		return null;
	}
}
