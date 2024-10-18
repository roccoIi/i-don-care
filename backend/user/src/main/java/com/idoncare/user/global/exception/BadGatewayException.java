package com.idoncare.user.global.exception;

import com.idoncare.user.global.common.exception.ErrorCode;

import lombok.Getter;

@Getter
public class BadGatewayException extends RuntimeException {
	private final ErrorCode errorCode;

	public BadGatewayException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
