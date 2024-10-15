package com.idoncare.user.unit.service;

import com.idoncare.user.application.SmsAuthService;
import com.idoncare.user.application.SmsService;
import com.idoncare.user.dto.req.LoginIntegrateVerifyReq;
import com.idoncare.user.entity.SmsAuthEntity;
import com.idoncare.user.global.exception.AuthorizedException;
import com.idoncare.user.global.common.util.TimeUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

	@Mock
	private SmsAuthService smsAuthService;

	@InjectMocks
	private SmsService smsService;

	// 공통 변수
	private String tell;
	private String correctCode;
	private String wrongCode;
	private LoginIntegrateVerifyReq loginVerifyReq;

	@BeforeEach
	void setUp() {
		tell = "01059363877";
		correctCode = "123456";
		wrongCode = "654321";

		loginVerifyReq = new LoginIntegrateVerifyReq();
		loginVerifyReq.setTell(tell);
	}

	@Test
	@DisplayName("성공적인 인증 코드 검증")
	void testProcessVerify_Success() {
		// Given
		loginVerifyReq.setCode(correctCode);

		// Mocking - 올바른 인증 코드와 만료되지 않은 시간
		SmsAuthEntity smsAuthEntity = SmsAuthEntity.builder()
			.tel(tell)
			.code(correctCode)
			.expireAt(TimeUtil.addMinutes(LocalDateTime.now(), 5))
			.build();

		// When
		when(smsAuthService.verifyCode(anyString(), any(LocalDateTime.class))).thenReturn(Optional.of(smsAuthEntity));

		// Then
		assertDoesNotThrow(() -> smsService.processVerify(loginVerifyReq));
		verify(smsAuthService, times(1)).verifyCode(anyString(), any(LocalDateTime.class));
	}

	@Test
	@DisplayName("인증 코드 만료 시 A002 예외 발생")
	void testProcessVerify_CodeExpired() {
		// Given
		loginVerifyReq.setCode(correctCode);

		// Mocking - 만료된 코드로 조회 실패
		when(smsAuthService.verifyCode(anyString(), any(LocalDateTime.class))).thenReturn(Optional.empty());

		// Then
		AuthorizedException exception = assertThrows(AuthorizedException.class, () -> smsService.processVerify(loginVerifyReq));
		assertEquals("A002", exception.getErrorCode().name());
		verify(smsAuthService, times(1)).verifyCode(anyString(), any(LocalDateTime.class));
	}

	@Test
	@DisplayName("잘못된 인증 코드 입력 시 A003 예외 발생")
	void testProcessVerify_IncorrectCode() {
		// Given
		loginVerifyReq.setCode(wrongCode);

		// Mocking - 유효한 코드가 있지만 사용자가 틀린 코드를 입력
		SmsAuthEntity smsAuthEntity = SmsAuthEntity.builder()
			.tel(tell)
			.code(correctCode)
			.expireAt(TimeUtil.addMinutes(LocalDateTime.now(), 5))
			.build();

		// When
		when(smsAuthService.verifyCode(anyString(), any(LocalDateTime.class))).thenReturn(Optional.of(smsAuthEntity));

		// Then
		AuthorizedException exception = assertThrows(AuthorizedException.class, () -> smsService.processVerify(loginVerifyReq));
		assertEquals("A003", exception.getErrorCode().name());
		verify(smsAuthService, times(1)).verifyCode(anyString(), any(LocalDateTime.class));
	}
}
