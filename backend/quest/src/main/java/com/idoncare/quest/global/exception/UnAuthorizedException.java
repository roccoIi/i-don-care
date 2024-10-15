package com.idoncare.quest.global.exception;

import com.idoncare.quest.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class UnAuthorizedException extends RuntimeException {
	private final ErrorCode errorCode;

	public UnAuthorizedException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
