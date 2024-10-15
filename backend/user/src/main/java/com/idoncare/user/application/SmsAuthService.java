package com.idoncare.user.application;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.idoncare.user.entity.SmsAuthEntity;
import com.idoncare.user.global.common.util.TimeUtil;
import com.idoncare.user.repository.SmsAuthRepository;

@Service
public class SmsAuthService {

	private final SmsAuthRepository smsAuthRepository;

	public SmsAuthService(SmsAuthRepository smsAuthRepository) {
		this.smsAuthRepository = smsAuthRepository;
	}

	public void saveVerificationCode(String phoneNumber, LocalDateTime expiredAt, String code) {
		smsAuthRepository.save(SmsAuthEntity.builder()
			.tel(phoneNumber)
			.code(code)
			.expireAt(expiredAt)
			.build()
		);
	}

	public Optional<SmsAuthEntity> verifyCode(String phoneNumber, LocalDateTime now) {
		return smsAuthRepository.findByTelAndExpireAtAfter(phoneNumber, now);
	}
}
