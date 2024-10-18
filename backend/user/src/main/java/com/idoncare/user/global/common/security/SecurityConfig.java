package com.idoncare.user.global.common.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.cors((corsCustomizer) -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration configuration = new CorsConfiguration();
					configuration.setAllowedOrigins(
						Arrays.asList("https://j11a603.p.ssafy.io", "http://j11a603.p.ssafy.io",
							"http://localhost:5173", "http://localhost:8080",
							"https://j11a603.p.ssafy.io:8080", "http://j11a603.p.ssafy.io:8080",
							"https://j11a603.p.ssafy.io:5173", "http://j11a603.p.ssafy.io:5173"
						));
					configuration.setAllowedMethods(Collections.singletonList("*"));
					configuration.setAllowCredentials(true);
					configuration.setAllowedHeaders(Collections.singletonList("*"));
					configuration.setMaxAge(3600L);
					configuration.setExposedHeaders(Collections.singletonList("Authorization"));
					return configuration;
				}
			}));

		// CSRF 비활성화 (내부 API 용도일 경우)
		http.csrf((auth) -> auth.disable());

		// 세션 설정 (stateless 설정)
		http.sessionManagement((session) -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// 모든 경로에 대해 허용
		http.authorizeHttpRequests((auth) -> auth
			.anyRequest().permitAll());

		return http.build();
	}
}
