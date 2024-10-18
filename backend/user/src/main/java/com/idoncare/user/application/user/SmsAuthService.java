package com.idoncare.user.application.user;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idoncare.user.entity.SmsAuthEntity;
import com.idoncare.user.repository.SmsAuthRepository;

@Service
public class SmsAuthService {

	@Autowired
	private SmsAuthRepository smsAuthRepository;

	@Transactional
	public void saveVerificationCode(final String phoneNumber, final LocalDateTime expiredAt, final String code) {
		smsAuthRepository.save(SmsAuthEntity.toEntity(
			phoneNumber, code, expiredAt
		));
	}

	@Transactional(readOnly = true)
	public Optional<SmsAuthEntity> verifyCode(final String phoneNumber, final LocalDateTime now) {
		return smsAuthRepository.findByTelAndExpireAtAfter(phoneNumber, now);
	}

	@Transactional
	public void deleteVerificationCode(final String phoneNumber) {
		smsAuthRepository.deleteByTel(phoneNumber);
	}
}
