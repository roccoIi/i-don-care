package com.idoncare.user.global.common.security;

import static io.jsonwebtoken.Jwts.*;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class JwtUtil {
	private final Key key;
	private final long accessTokenExpTime;
	private final long refreshTokenExpTime;

	public JwtUtil(
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.access_token.expiration_time}") long accessTokenExpTime,
		@Value("${jwt.refresh_token.expiration_time}") long refreshTokenExpTime
	) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.accessTokenExpTime = accessTokenExpTime;
		this.refreshTokenExpTime = refreshTokenExpTime;
	}

	/**
	 * JWT 생성
	 * @param userId
	 * @param now
	 * @param expireTime
	 * @return JWT String
	 */
	public String createToken(Long userId, LocalDateTime now, LocalDateTime expireTime) {
		Claims claims = claims();
		claims.put("userId", userId);

		Date issuedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		Date expiration = Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant());

		return builder()
			.setClaims(claims)
			.setIssuedAt(issuedAt)          // 발급 시간 설정
			.setExpiration(expiration)      // 만료 시간 설정
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	/**
	 * Token에서 User ID 추출
	 * @param token
	 * @return User ID
	 */
	public Long getUserId(String token) {
		return parseClaims(token).get("userId", Long.class);
	}

	/**
	 * JWT Claims 추출
	 * @param accessToken
	 * @return JWT Claims
	 */
	public Claims parseClaims(String accessToken) {
		try {
			return parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

}
