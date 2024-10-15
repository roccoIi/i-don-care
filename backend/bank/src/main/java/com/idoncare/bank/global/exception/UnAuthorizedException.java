package com.idoncare.bank.global.exception;


import com.idoncare.bank.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class UnAuthorizedException extends RuntimeException {
	private final ErrorCode errorCode;

	public UnAuthorizedException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
