package com.idoncare.bank.global.exception;

import com.idoncare.bank.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class JsonParsingException extends RuntimeException {
	private ErrorCode errorCode;

	public JsonParsingException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
