package com.idoncare.user.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idoncare.user.entity.SmsAuthEntity;

public interface SmsAuthRepository extends JpaRepository<SmsAuthEntity, Long> {
	Optional<SmsAuthEntity> findByTel(String tel);
	
	@Query("SELECT s FROM SmsAuthEntity s WHERE s.tel = :tel AND s.expireAt > :currentTime")
	Optional<SmsAuthEntity> findByTelAndExpireAtAfter(String tel, LocalDateTime currentTime);

	void deleteByTel(String tel);
}
