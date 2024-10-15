package com.idoncare.bank.global.exception;


import com.idoncare.bank.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class TimeOutException extends RuntimeException {
	private ErrorCode errorCode;

	public TimeOutException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}

