package com.idoncare.bank.global.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.idoncare.bank.domain.account.dto.comm.RequestHeader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HeaderUtil {

	@Value("${my.apiKey}")
	private String apiKey;

	@Value("${my.userKey}")
	private String userKey;

	public RequestHeader makeHeadWithApi(String url){
		return RequestHeader.builder()
			.apiName(url)
			.transmissionDate(nowDate())
			.transmissionTime(nowTime())
			.institutionCode("00100")
			.fintechAppNo("001")
			.apiServiceCode(url)
			.institutionTransactionUniqueNo(makeUniqueNo())
			.apiKey(apiKey)
			.build();
	}

	public RequestHeader makeHeadWithUserKey(String url){
		return RequestHeader.builder()
			.apiName(url)
			.transmissionDate(nowDate())
			.transmissionTime(nowTime())
			.institutionCode("00100")
			.fintechAppNo("001")
			.apiServiceCode(url)
			.institutionTransactionUniqueNo(makeUniqueNo())
			.apiKey(apiKey)
			.userKey(userKey)
			.build();
	}

	public String makeUniqueNo(){
		// SecureRandom을 사용하여 더 안전한 난수 생성
		SecureRandom random = new SecureRandom();

		// 20자리 숫자를 생성
		BigInteger bigInteger = new BigInteger(20 * 3, random); // 20자리 숫자를 얻기 위해 60비트의 난수를 생성

		// 20자리 숫자로 변환
		String random20DigitNumber = bigInteger.toString();

		// 20자리 숫자 보장하기 위해 길이를 조정
		if (random20DigitNumber.length() > 20) {
			random20DigitNumber = random20DigitNumber.substring(0, 20);
		} else if (random20DigitNumber.length() < 20) {
			random20DigitNumber = String.format("%020d", new BigInteger(random20DigitNumber));
		}

		return random20DigitNumber;
	}

	public String nowDate(){
		// 현재 날짜를 가져옴
		LocalDate today = LocalDate.now();

		// 날짜를 YYYYMMDD 형식으로 포맷팅하기 위한 포맷터 정의
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		// 날짜를 문자열로 변환후 반환
		return today.format(formatter);
	}

	DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


	public String nowTime(){
		// 현재 시간을 가져옴
		LocalTime now = LocalTime.now();

		// 시간 포맷터 정의 (HHMMSS 형식)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");

		// 시간을 문자열로 변환후 반환
		return now.format(formatter);
	}


}
