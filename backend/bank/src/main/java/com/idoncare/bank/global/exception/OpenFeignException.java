package com.idoncare.bank.global.exception;

import com.idoncare.bank.global.exception.decoder.SsafyErrorResponse;

import lombok.Getter;

@Getter
public class OpenFeignException extends RuntimeException{
	private final String responseCode;
	private final String responseMessage;

	public OpenFeignException(SsafyErrorResponse ssafyErrorResponse) {
		super(ssafyErrorResponse.getResponseMessage());
		this.responseCode = ssafyErrorResponse.getResponseCode();
		this.responseMessage = "[SSAFY] " + ssafyErrorResponse.getResponseMessage();
	}
}
