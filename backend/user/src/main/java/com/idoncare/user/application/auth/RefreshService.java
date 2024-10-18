package com.idoncare.user.application.auth;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idoncare.user.entity.RefreshTokenEntity;
import com.idoncare.user.global.common.security.JwtUtil;
import com.idoncare.user.repository.RefreshRepository;

@Service
public class RefreshService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RefreshRepository refreshRepository;

	/**
	 * 리프레시 토큰으로 엔티티 조회
	 * @param refreshToken
	 * @return RefreshTokenEntity or null
	 */
	public RefreshTokenEntity findByRefreshToken(String refreshToken) {
		return refreshRepository.findByRefreshToken(refreshToken)
			.orElse(null);
	}

	/**
	 * 리프레시 토큰 삭제
	 * @param refreshToken
	 */
	public void deleteRefreshToken(String refreshToken) {
		refreshRepository.deleteByRefreshToken(refreshToken);
	}

	/**
	 * 리프레시 토큰 생성 및 저장
	 *
	 * @param userId
	 * @param now
	 * @param expireTime
	 * @return 생성된 RefreshTokenEntity
	 */
	public RefreshTokenEntity createAndSaveRefreshToken(Long userId, LocalDateTime now,
		LocalDateTime expireTime) {

		// 리프레시 토큰 생성
		String refreshToken = jwtUtil.createToken(userId, now, expireTime);

		// 리프레시 토큰 저장
		return refreshRepository.save(
			RefreshTokenEntity.builder()
				.refreshToken(refreshToken)
				.userId(userId)
				.expiryDate(expireTime)
				.build()
		);
	}

}
