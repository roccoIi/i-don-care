package com.idoncare.user.integration.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.idoncare.user.application.user.SmsAuthService;
import com.idoncare.user.entity.SmsAuthEntity;
import com.idoncare.user.repository.SmsAuthRepository;

@SpringBootTest
public class SmsAuthServiceTest {

	@Autowired
	private SmsAuthService smsAuthService;

	@Autowired
	private SmsAuthRepository smsAuthRepository;

	@Test
	@Transactional
	@DisplayName("휴대전화 인증 코드를 저장하고, 정상적으로 저장되었는지 확인")
	public void testSaveVerificationCode() {
		// Given
		String phoneNumber = "01012345678";
		String code = "123456";
		LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

		// When
		smsAuthService.saveVerificationCode(phoneNumber, expiredAt, code);

		// Then
		Optional<SmsAuthEntity> savedEntity = smsAuthRepository.findByTelAndExpireAtAfter(
			phoneNumber,
			LocalDateTime.now().plusMinutes(1)
		);
		assertThat(savedEntity).isPresent();
		assertThat(savedEntity.get().getTel()).isEqualTo(phoneNumber);
		assertThat(savedEntity.get().getCode()).isEqualTo(code);
		assertThat(savedEntity.get().getExpireAt()).isAfter(LocalDateTime.now());
	}

	@Test
	@Transactional
	@DisplayName("휴대전화 인증 코드를 소프트 삭제하고, 정상적으로 삭제되었는지 확인 - soft delete")
	public void testSoftDeleteVerificationCode() {
		// Given
		String phoneNumber = "01012345678";
		String code = "123456";
		LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

		// 먼저 저장
		smsAuthService.saveVerificationCode(phoneNumber, expiredAt, code);

		// When
		smsAuthService.deleteVerificationCode(phoneNumber);

		// Then
		Optional<SmsAuthEntity> deleteEntity = smsAuthRepository.findByTelAndExpireAtAfter(
			phoneNumber,
			LocalDateTime.now().plusMinutes(1)
		);
		assertThat(deleteEntity).isNotPresent();
	}
}
