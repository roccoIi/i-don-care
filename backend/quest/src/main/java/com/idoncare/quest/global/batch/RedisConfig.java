package com.idoncare.quest.global.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.idoncare.quest.domain.quiz.dto.QuizDtoImpl;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, QuizDtoImpl> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, QuizDtoImpl> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 지원
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO 형식으로 날짜를 직렬화

		// 커스텀 ObjectMapper를 사용하는 GenericJackson2JsonRedisSerializer 설정
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		// 키는 String, 값은 JSON 직렬화를 사용
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(serializer);

		return template;
	}
}
