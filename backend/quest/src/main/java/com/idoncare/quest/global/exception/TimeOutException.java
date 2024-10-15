package com.idoncare.quest.global.exception;

import com.idoncare.quest.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class TimeOutException extends RuntimeException {
	private ErrorCode errorCode;

	public TimeOutException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}

