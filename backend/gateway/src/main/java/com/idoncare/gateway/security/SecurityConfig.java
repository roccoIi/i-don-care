package com.idoncare.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import jakarta.validation.constraints.NotNull;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(@NotNull ServerHttpSecurity http) {
		http
			.csrf(csrf -> csrf.disable())  // CSRF 비활성화
			.authorizeExchange(exchanges -> exchanges
				.anyExchange().permitAll()  // 모든 요청을 허용
			)
			.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())  // SecurityContext 비활성화
			.logout(logout -> logout.disable());  // 로그아웃 비활성화

		return http.build();
	}
}
