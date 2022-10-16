package com.ddd.what2eat.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ddd.what2eat.security.EntryPointUnauthorizedHandler;
import com.ddd.what2eat.security.Jwt;
import com.ddd.what2eat.security.JwtAccessDeniedHandler;
import com.ddd.what2eat.security.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final Jwt jwt;

	private final JwtTokenConfig jwtTokenConfig;

	private final JwtAccessDeniedHandler accessDeniedHandler;

	private final EntryPointUnauthorizedHandler unauthorizedHandler;

	public WebSecurityConfig(Jwt jwt, JwtTokenConfig jwtTokenConfig, JwtAccessDeniedHandler accessDeniedHandler,
		EntryPointUnauthorizedHandler unauthorizedHandler) {
		this.jwt = jwt;
		this.jwtTokenConfig = jwtTokenConfig;
		this.accessDeniedHandler = accessDeniedHandler;
		this.unauthorizedHandler = unauthorizedHandler;
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/static/**", "/templates/**", "/h2-console");
	}

	// @Autowired
	// public void configureAuthentication(AuthenticationManagerBuilder builder,
	// 	JwtAuthenticationProvider authenticationProvider) {
	// 	builder.authenticationProvider(authenticationProvider);
	// }

	// @Bean
	// public JwtAuthenticationProvider jwtAuthenticationProvider(Jwt jwt, UserService userService) {
	// 	return new JwtAuthenticationProvider(jwt, userService);
	// }

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
			.headers()
			.disable()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler)
			.authenticationEntryPoint(unauthorizedHandler)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/img/**").permitAll()
			.antMatchers("/what2EatLogo.png").permitAll()
			.antMatchers("/create").permitAll()
			.antMatchers("/attendances/**").permitAll()
			.antMatchers("/api/users/**").permitAll()
			.antMatchers("/h2-console/**").permitAll()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.disable();
		http
			.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	}

	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
		return new JwtAuthenticationTokenFilter(jwtTokenConfig.getHeader(), jwt);
	}
}
