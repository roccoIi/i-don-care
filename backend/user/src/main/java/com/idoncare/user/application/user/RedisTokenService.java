package com.idoncare.user.application.user;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenService {

	private final RedisTemplate<String, String> redisTemplate;

	public RedisTokenService(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void saveRefreshToken(String username, String refreshToken) {
		redisTemplate.opsForValue().set(username, refreshToken, 86400000, TimeUnit.MILLISECONDS);
	}

	public String getRefreshToken(String username) {
		return redisTemplate.opsForValue().get(username);
	}

	public void deleteRefreshToken(String username) {
		redisTemplate.delete(username);
	}
}

