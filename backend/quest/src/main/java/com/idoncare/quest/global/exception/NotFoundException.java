package com.idoncare.quest.global.exception;

import com.idoncare.quest.global.common.ErrorCode;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
	private ErrorCode errorCode;

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
