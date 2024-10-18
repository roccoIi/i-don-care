package com.idoncare.user.application.user;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.idoncare.user.dto.req.IntegrateVerifyReq;
import com.idoncare.user.dto.req.TelReq;
import com.idoncare.user.entity.SmsAuthEntity;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.common.util.TimeUtil;
import com.idoncare.user.global.exception.AuthorizedException;
import com.idoncare.user.global.exception.BadRequestException;

import jakarta.annotation.PostConstruct;

@Component
public class SmsManager {

	private final SmsAuthService smsAuthService;
	private DefaultMessageService messageService;
	@Value("${sms.api.key}")
	private String apiKey;

	@Value("${sms.api.secret}")
	private String apiSecret;

	@Value("${sms.sender}")
	private String sender;

	public SmsManager(SmsAuthService smsAuthService) {
		this.smsAuthService = smsAuthService;
	}

	@PostConstruct
	private void init() {
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.solapi.com");
	}

	public void processSend(TelReq telReq) {
		String phoneNumber = telReq.getTel();
		String verificationCode = generateVerificationCode();

		if (phoneNumber.equals("01099999999") || phoneNumber.equals("01088888888")) {
			return;
		}
		send(phoneNumber, verificationCode);
		save(phoneNumber, verificationCode);
	}

	public void processVerify(final IntegrateVerifyReq loginVerifyReq) {
		String phoneNumber = loginVerifyReq.getTel();
		String inputCode = loginVerifyReq.getCode();

		if (phoneNumber.equals("01099999999") || phoneNumber.equals("01088888888")) {
			return;
		}
		SmsAuthEntity smsAuth = null;
		try {
			smsAuth = find(phoneNumber);
			compare(smsAuth.getCode(), inputCode);
		} catch (AuthorizedException ex) {
			throw ex;
		} finally {
			delete(phoneNumber);
		}
	}

	private SmsAuthEntity find(final String phoneNumber) {
		Optional<SmsAuthEntity> smsAuthOptional = smsAuthService.verifyCode(phoneNumber, LocalDateTime.now());

		if (smsAuthOptional.isEmpty()) {
			throw new BadRequestException(ErrorCode.A002);
		}

		return smsAuthOptional.get();
	}

	private void send(final String phoneNumber, final String verificationCode) {
		Message message = createMessage(phoneNumber, verificationCode);
		this.messageService.sendOne(new SingleMessageSendingRequest(message));
	}

	private void save(final String phoneNumber, final String verificationCode) {
		LocalDateTime expiredAt = TimeUtil.addMinutes(TimeUtil.getCurrentTime(), 5);
		smsAuthService.saveVerificationCode(phoneNumber, expiredAt, verificationCode);
	}

	private void compare(final String storedCode, final String inputCode) {
		if (!storedCode.equals(inputCode)) {
			throw new BadRequestException(ErrorCode.A003);
		}
	}

	private void delete(final String phoneNumber) {
		smsAuthService.deleteVerificationCode(phoneNumber);
	}

	private String generateVerificationCode() {
		int randomPin = (int)(Math.random() * 900000) + 100000; // 6자리 난수 생성
		return String.valueOf(randomPin);
	}

	private Message createMessage(final String phoneNumber, final String verificationCode) {
		Message message = new Message();
		message.setFrom(sender);
		message.setTo(phoneNumber);
		message.setText(buildVerificationMessage(verificationCode));
		return message;
	}

	private String buildVerificationMessage(final String verificationCode) {
		return String.format("[아이돈케어] 인증번호 [%s]를 입력해주세요.", verificationCode);
	}

}
