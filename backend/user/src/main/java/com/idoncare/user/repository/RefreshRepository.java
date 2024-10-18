package com.idoncare.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idoncare.user.entity.RefreshTokenEntity;

public interface RefreshRepository extends JpaRepository<RefreshTokenEntity, Long> {
	Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

	void deleteByRefreshToken(String refreshToken);
}
