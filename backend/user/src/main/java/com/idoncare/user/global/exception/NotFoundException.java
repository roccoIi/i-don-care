package com.idoncare.user.global.exception;

import com.idoncare.user.global.common.exception.ErrorCode;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
	private final ErrorCode errorCode;

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
