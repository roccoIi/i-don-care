package com.idoncare.gateway.exception;

import lombok.Getter;

@Getter
public class AuthorizedException extends RuntimeException {
	private final ErrorCode errorCode;

	public AuthorizedException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
