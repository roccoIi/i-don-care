package com.idoncare.user.application.auth;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.idoncare.user.domain.user.User;
import com.idoncare.user.dto.req.IntegrateSignUpReq;
import com.idoncare.user.entity.RefreshTokenEntity;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.common.security.JwtUtil;
import com.idoncare.user.global.exception.AuthorizedException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthManager {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthService authService;

	@Autowired
	private RefreshService refreshService;

	@Transactional
	public void createRefreshToken(HttpServletResponse response,
		String tel) {
		User user = authService.findUserByTel(tel);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime refreshTokenExpireTime = now.plusSeconds(jwtUtil.getRefreshTokenExpTime());

		// Access Token 및 Refresh Token 생성
		String refreshToken = jwtUtil.createToken(user.getUserId(), now, refreshTokenExpireTime);
		System.out.println("DB에 저장하는 refresh token : " + refreshToken);

		// Refresh Token을 데이터베이스에 저장
		RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
			.refreshToken(refreshToken)
			.userId(user.getUserId())
			.expiryDate(refreshTokenExpireTime) // JWT와 동일한 만료시간 사용
			.build();
		refreshService.createAndSaveRefreshToken(
			refreshTokenEntity.getUserId(),
			now,
			refreshTokenExpireTime
		);

		// 쿠키 생성 및 응답에 추가 (Set-Cookie 헤더로 처리)
		response.setHeader("Set-Cookie", "refresh=" + refreshToken
			+ "; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=" + (7 * 24 * 60 * 60));
	}

	// // 쿠키 생성 로직
	// private Cookie createRefreshTokenCookie(String refreshToken) {
	// 	Cookie refreshTokenCookie = new Cookie("refresh", refreshToken);
	// 	refreshTokenCookie.setHttpOnly(true);
	//
	// 	refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
	// 	refreshTokenCookie.setPath("/");
	// 	return refreshTokenCookie;
	// }

	@Transactional
	public Long getUserIdFromRefreshToken(HttpServletRequest req) {
		String refreshToken = extractRefreshToken(req.getCookies());

		// DB에서 refresh token 찾기
		Long userId = jwtUtil.getUserId(refreshToken);
		System.out.println("리프레시 토큰으로부터 얻은 USER ID는 : " + userId);
		return userId;
	}

	@Transactional
	public String refreshAccessToken(HttpServletRequest req) {
		String refreshToken = extractRefreshToken(req.getCookies());

		// DB에서 refresh token 찾기
		Long userId = jwtUtil.getUserId(refreshToken);
		RefreshTokenEntity storedRefreshToken = refreshService.findByRefreshToken(refreshToken);
		if (storedRefreshToken == null || !storedRefreshToken.getUserId().equals(userId)) {
			throw new AuthorizedException(ErrorCode.A006);
		}

		// Access Token 재발급
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime newAccessTokenExpireTime = now.plusSeconds(jwtUtil.getAccessTokenExpTime());

		// 새 Access Token 반환
		return jwtUtil.createToken(userId, now, newAccessTokenExpireTime);
	}

	private String extractRefreshToken(Cookie[] cookies) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("refresh")) {
					System.out.println(cookie.getValue());
					return cookie.getValue(); // "refresh" 쿠키의 값을 반환
				}
			}
		}
		throw new AuthorizedException(ErrorCode.A006);
	}

	@Transactional
	public void deleteRefreshToken(HttpServletRequest req, HttpServletResponse res) {
		// 1. Request에서 Refresh Token 추출
		String refreshToken = extractRefreshToken(req.getCookies());

		// 2. DB에서 Refresh Token 삭제
		RefreshTokenEntity storedRefreshToken = refreshService.findByRefreshToken(refreshToken);
		if (storedRefreshToken != null) {
			refreshService.deleteRefreshToken(storedRefreshToken.getRefreshToken());
			System.out.println("DB에서 리프레시 토큰 삭제 완료.");
		}

		// 3. 클라이언트 측에서 쿠키 삭제 (만료 처리)
		Cookie refreshCookie = new Cookie("refresh", null); // 쿠키 값 null로 설정
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(true);
		refreshCookie.setPath("/");
		refreshCookie.setMaxAge(0);

		res.addCookie(refreshCookie);
		System.out.println("클라이언트의 리프레시 토큰 쿠키 삭제 완료.");
	}
}
