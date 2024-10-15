package com.idoncare.gateway.security;

import static io.jsonwebtoken.Jwts.*;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.idoncare.gateway.exception.AuthorizedException;
import com.idoncare.gateway.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class JwtUtil {
	private final Key key;

	public JwtUtil(
		@Value("${jwt.secret}") String secretKey
	) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * JWT 검증
	 * @return IsValidate
	 */
	public void validateToken(String token) {
		try {
			parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		} catch (SecurityException | MalformedJwtException e) {
			log.info("[A006] 유효하지 않은 토큰입니다", e);
			throw new AuthorizedException(ErrorCode.A006); // JWT 형식이 잘못됨
		} catch (ExpiredJwtException e) {
			log.info("[A008] 인증/인가시, 만료된 토큰입니다", e);
			throw new AuthorizedException(ErrorCode.A008); // 만료된 JWT
		} catch (UnsupportedJwtException e) {
			log.info("[A007] 인증/인가시, 지원되지 않는 토큰입니다.", e);
			throw new AuthorizedException(ErrorCode.A007); // 지원되지 않는 JWT
		} catch (IllegalArgumentException e) {
			log.info("[A007] 형식에 맞지 않는 토큰입니다.", e);
			throw new AuthorizedException(ErrorCode.A007); // 잘못된 JWT
		}
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

	public Long extractUserId(String jwt) {
		Claims claims = parseClaims(jwt);
		return claims.get("userId", Long.class);
	}

}
