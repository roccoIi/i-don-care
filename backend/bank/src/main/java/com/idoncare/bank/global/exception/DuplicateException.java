package com.idoncare.bank.global.exception;

import com.idoncare.bank.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException{
	private ErrorCode errorCode;

	public DuplicateException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
