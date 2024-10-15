package com.idoncare.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.idoncare.gateway.exception.AuthorizedException;
import com.idoncare.gateway.exception.ErrorCode;
import com.idoncare.gateway.security.JwtUtil;

import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Slf4j
@Component
public class JwtFilter implements GlobalFilter, Ordered {

	private final JwtUtil jwtUtil;

	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}


	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("------------- JwtFilter Start ---------------");

		if (exchange == null || chain == null) {
			return Mono.empty();
		}

		String requestURI = exchange.getRequest().getURI().getPath();
		log.info("Request URI: {}", requestURI);

		// 특정 URI는 필터를 통과
		if (requestURI.startsWith("/api/user/integrate") || requestURI.startsWith("/api/user/simple") ||
			requestURI.startsWith("/api/user/reissue") || requestURI.startsWith("/api/quest/v3/api-docs") ||
			requestURI.startsWith("/api/bank/v3/api-docs") || requestURI.startsWith("/api/user/v3/api-docs") ||
			requestURI.startsWith("/api/bank/inner") || requestURI.startsWith("/api/user/inner") ||
			requestURI.startsWith("/api/quest/inner")) {

			return chain.filter(exchange);
		}

		final String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
		String jwt = getTokenFromHeader(authorizationHeader);

		if (jwt == null) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		try {
			Long userId = jwtUtil.extractUserId(jwt); // userId 추출
			if (userId == null) {
				throw new AuthorizedException(ErrorCode.A010);
			}

			// JWT 토큰 유효성 검증
			jwtUtil.validateToken(jwt);

			// **ServerHttpRequest**를 사용하여 헤더 추가
			ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
				.header("X-User-Id", String.valueOf(userId))
				.build();

			ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

			return chain.filter(modifiedExchange);  // 수정된 요청을 전달
		} catch (AuthorizedException ex) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
	}

	private String getTokenFromHeader(@Nullable String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7); // Bearer 이후의 토큰만 추출
		}
		return null;
	}

	@Override
	public int getOrder() {
		return -3;
	}
}
