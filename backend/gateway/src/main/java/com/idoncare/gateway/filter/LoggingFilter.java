package com.idoncare.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("------------- LoggingFilter Start ---------------");

		try {
			String requestPath = exchange.getRequest().getPath().toString();
			Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
			String routeId = (route != null) ? route.getId() : "null";
			log.info("요청 PATH: {}", requestPath);
			log.info("라우트 ID: {}", routeId);
		} catch (Exception e) {
			log.error("필터 처리 중 오류 발생: {}", e.getMessage(), e);
		}
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
