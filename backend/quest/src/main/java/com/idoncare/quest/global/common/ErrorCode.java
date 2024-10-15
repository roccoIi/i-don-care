package com.idoncare.quest.global.common;

public enum ErrorCode {
	// AUTH 관련 예외 코드
	A000("~~할 경우, ~~ 예외 발생"),

	// USER 관련 예외 코드
	U000("~~할 경우, ~~ 예외 발생"),

	// QUEST 관련 예외 코드
	Q000("문제 조회 시, 존재하지 않는 관계"),
	Q001("관계 추가 시, 이미 존재하는 관계"),
	Q002("API 요청시, 포맷을 지키지 않으면 예외 발생"),
	Q003("저금통 조회 시, 등록된 저금통이 존재하지 않아 예외 발생"),
	Q004("미션 제출 시, 이미지를 선택하지 않아 예외 발생"),
	Q005("S3에 이미지 저장 시, 오류 발생"),
	Q006("인증/인가시, 엑세스 토큰이 존재하지 않음"),
	Q007("저금통 저금 시 현재 금액보다 많은 금액이 입력되어 발생"),
	Q008("관계 조회 시, 존재하지 않는 관계"),

	// BANK 관련 예외 코드
	B000("~~할 경우, ~~ 예외 발생"),

	// Global 예외
	G000("~~할 경우, ~~ 예외 발생");

	private final String message;

	ErrorCode(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
