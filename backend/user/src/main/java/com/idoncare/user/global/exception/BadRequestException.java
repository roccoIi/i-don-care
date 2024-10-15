package com.idoncare.user.global.exception;

import com.idoncare.user.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
	private final ErrorCode errorCode;

	public BadRequestException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
