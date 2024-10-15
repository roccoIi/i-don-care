package com.idoncare.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.gateway.utils.RSAUtil;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
@Slf4j
@Component
public class DecryptionFilter implements GlobalFilter, Ordered {

	@Autowired
	private RSAUtil rsaUtil;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("------------- DecryptionFilter Start ---------------");

		ServerHttpRequest request = exchange.getRequest();
		if (request.getMethod() != HttpMethod.POST && request.getMethod() != HttpMethod.PATCH
			&& request.getMethod() != HttpMethod.PUT) {
			return chain.filter(exchange); // GET 요청은 복호화 필요 없음
		}

		// Request Body 읽기 및 처리
		return DataBufferUtils.join(request.getBody())
			.defaultIfEmpty(exchange.getResponse().bufferFactory().wrap(new byte[0])) // 빈 데이터 처리
			.flatMap(dataBuffer -> {
				byte[] bytes = new byte[dataBuffer.readableByteCount()];
				dataBuffer.read(bytes);
				DataBufferUtils.release(dataBuffer); // 메모리 누수 방지
				String body = new String(bytes, StandardCharsets.UTF_8);

				log.info("Request Body: {}", body);
				if (body.trim().isEmpty() || body.equals("{}")) {
					log.info("Empty or {} body, passing through the filter.");
					return chain.filter(exchange); // 비어있는 경우 필터 통과
				}

				try {
					// JSON 파싱 및 복호화
					JsonNode jsonNode = objectMapper.readTree(body);

					if (jsonNode.has("encrypted")) {
						// 'encrypted' 필드가 boolean이면 통과
						if (jsonNode.get("encrypted").isBoolean()) {
							boolean encryptedData = jsonNode.get("encrypted").asBoolean();
							if (!encryptedData) {
								log.info("'encrypted' field is false, passing through the filter.");
								return chain.filter(exchange); // 복호화 필요 없음
							}
						}
						// 'encrypted' 필드가 문자열이라면 복호화 시도
						else if (jsonNode.get("encrypted").isTextual()) {
							String encryptedData = jsonNode.get("encrypted").asText();
							String decryptedBody = rsaUtil.decrypt(encryptedData);

							log.info("Decrypted body: {}", decryptedBody);

							// 새로운 요청 본문으로 설정
							byte[] newRequestBodyBytes = decryptedBody.getBytes(StandardCharsets.UTF_8);
							DataBuffer newDataBuffer = exchange.getResponse().bufferFactory().wrap(newRequestBodyBytes);

							// 수정된 요청 생성
							ServerHttpRequest modifiedRequest = new ServerHttpRequestDecorator(request) {
								@Override
								public Flux<DataBuffer> getBody() {
									return Flux.just(newDataBuffer); // 복호화된 바디로 요청 설정
								}

								@Override
								public HttpHeaders getHeaders() {
									HttpHeaders headers = new HttpHeaders();
									headers.putAll(super.getHeaders());
									headers.set(HttpHeaders.CONTENT_TYPE, "application/json"); // JSON으로 설정
									headers.setContentLength(newRequestBodyBytes.length); // 새 길이 설정
									return headers;
								}
							};

							// 필터 체인에 수정된 요청 전달
							return chain.filter(exchange.mutate().request(modifiedRequest).build());
						} else {
							log.error("'encrypted' field is of unknown type, passing through the filter.");
							return chain.filter(exchange); // 알 수 없는 타입일 경우 필터 통과
						}
					} else {
						log.error("'encrypted' field is missing in the request body");
						return chain.filter(exchange); // 'encrypted' 필드가 없을 경우 필터 통과
					}
				} catch (Exception e) {
					log.error("Decryption or JSON parsing failed", e);
					return Mono.error(new RuntimeException("Decryption or JSON parsing failed"));
				}
				return chain.filter(exchange);
			})
			.onErrorResume(e -> {
				log.error("Error in DecryptionFilter: {}", e.getMessage());
				return chain.filter(exchange); // 에러 발생 시에도 필터 통과
			});
	}

	@Override
	public int getOrder() {
		return -5; // 우선순위 설정: RequestBody를 캐싱하는 필터 다음에 실행되도록 설정
	}
}
