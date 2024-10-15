package com.idoncare.user.global.common;

public enum ErrorCode {
	// AUTH 관련 예외 코드
	A000("~~할 경우, ~~ 예외 발생"),
	A001("SMS 인증시, SMS 전송 실패"),
	A002("SMS 인증시, 유효시간 초과"),
	A003("SMS 인증시, 입력 인증번호 틀림"),
	// USER 관련 예외 코드
	U000("~~할 경우, ~~ 예외 발생"),

	// QUEST 관련 예외 코드
	Q000("~~할 경우, ~~ 예외 발생"),

	// BANK 관련 예외 코드
	B000("~~할 경우, ~~ 예외 발생"),

	// Global 예외
	G000("API 요청시, 포맷을 지키지 않으면 예외 발생"),

	// Valid 예외
	// V000("")

	;
	private final String message;

	ErrorCode(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
