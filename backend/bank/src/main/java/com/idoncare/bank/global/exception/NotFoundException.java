package com.idoncare.bank.global.exception;


import com.idoncare.bank.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
	private ErrorCode errorCode;

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
