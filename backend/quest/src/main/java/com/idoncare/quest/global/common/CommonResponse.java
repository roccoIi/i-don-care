package com.idoncare.quest.global.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CommonResponse<T> {
	private String code;
	private String message;
	private T data;

	public CommonResponse(String message, String code, T data) {
		this.message = message;
		this.code = code;
		this.data = data;
	}

	public static <T> CommonResponse<T> of(String message, String code) {
		return new CommonResponse<>(message, code, null);
	}

	public static <T> CommonResponse<T> of(ErrorCode responseCode, T data) {
		return new CommonResponse<>(responseCode.getMessage(), responseCode.name(), data);
	}

	public static <T> CommonResponse<T> from(T data) {
		return new CommonResponse<>("SUCCESS", null, data);
	}
}

