package com.idoncare.user.application;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import io.github.cdimascio.dotenv.Dotenv;

import com.idoncare.user.dto.req.LoginIntegrateVerifyReq;
import com.idoncare.user.dto.req.LoginTelReq;
import com.idoncare.user.entity.SmsAuthEntity;
import com.idoncare.user.global.common.ErrorCode;
import com.idoncare.user.global.common.util.TimeUtil;
import com.idoncare.user.global.exception.AuthorizedException;
import com.idoncare.user.repository.SmsAuthRepository;

@Service
public class SmsService {

	private final DefaultMessageService messageService;
	private final SmsAuthService smsAuthService;

	private Dotenv dotenv = Dotenv.load();

	String apiKey = dotenv.get("SMS_API_KEY");
	String apiSecret = dotenv.get("SMS_API_SECRET");
	String sender = dotenv.get("SMS_SENDER");

	public SmsService(SmsAuthService smsAuthService)  {
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
		this.smsAuthService = smsAuthService;
	}

	public void processSend(LoginTelReq loginTelReq) {
		String phoneNumber = loginTelReq.getTel();
		String verificationCode = generateVerificationCode();

		send(phoneNumber, verificationCode);
		save(phoneNumber, verificationCode);
	}

	public void processVerify(LoginIntegrateVerifyReq loginVerifyReq) {
		String phoneNumber = loginVerifyReq.getTell();
		String inputCode = loginVerifyReq.getCode();

		SmsAuthEntity smsAuth = find(phoneNumber);  // 인증 코드 조회
		compare(smsAuth.getCode(), inputCode);
	}

	private SmsAuthEntity find(String phoneNumber) {
		Optional<SmsAuthEntity> smsAuthOptional = smsAuthService.verifyCode(phoneNumber, LocalDateTime.now());

		if (smsAuthOptional.isEmpty()) {
			throw new AuthorizedException(ErrorCode.A002);
		}

		return smsAuthOptional.get();
	}

	private void send(String phoneNumber, String verificationCode) {
		Message message = createMessage(phoneNumber, verificationCode);
		this.messageService.sendOne(new SingleMessageSendingRequest(message));
	}

	private void save(String phoneNumber, String verificationCode) {
		LocalDateTime expiredAt = TimeUtil.addMinutes(TimeUtil.getCurrentTime(), 5);
		smsAuthService.saveVerificationCode(phoneNumber, expiredAt, verificationCode);
	}

	private void compare(String storedCode, String inputCode) {
		if (!storedCode.equals(inputCode)) {
			throw new AuthorizedException(ErrorCode.A003);
		}
	}

	private String generateVerificationCode() {
		int randomPin = (int) (Math.random() * 900000) + 100000; // 6자리 난수 생성
		return String.valueOf(randomPin);
	}

	private Message createMessage(String phoneNumber, String verificationCode) {
		Message message = new Message();
		message.setFrom(sender);
		message.setTo(phoneNumber);
		message.setText(buildVerificationMessage(verificationCode));
		return message;
	}

	private String buildVerificationMessage(String verificationCode) {
		return String.format("[아이돈케어] 인증번호 [%s]를 입력해주세요.", verificationCode);
	}

}
