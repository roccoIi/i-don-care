package com.idoncare.quest.global.exception;

import com.idoncare.quest.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
	private ErrorCode errorCode;

	public BadRequestException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
