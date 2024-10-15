package com.idoncare.user.global.exception;

import com.idoncare.user.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class AuthorizedException extends RuntimeException {
	private final ErrorCode errorCode;

	public AuthorizedException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
