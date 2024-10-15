package com.idoncare.user.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idoncare.user.entity.SmsAuthEntity;

public interface SmsAuthRepository extends JpaRepository<SmsAuthEntity, Long> {
	Optional<SmsAuthEntity> findByTel(String tel);
	Optional<SmsAuthEntity> findByTelAndExpireAtAfter(String tel, LocalDateTime currentTime);

}
