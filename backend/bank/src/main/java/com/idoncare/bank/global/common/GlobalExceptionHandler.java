package com.idoncare.bank.global.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idoncare.bank.global.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<CommonResponse> handleNotFoundException(final NotFoundException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(CommonResponse.of(
				errorCode.getMessage(),
				errorCode.name()
			));
	}

	@ExceptionHandler(OpenFeignException.class)
	public final ResponseEntity<CommonResponse> handleOpenFeignException(final OpenFeignException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(CommonResponse.of(
				e.getResponseMessage(),
				e.getResponseCode()
			));
	}

	@ExceptionHandler(DuplicateException.class)
	public final ResponseEntity<CommonResponse> handleDuplicateException(final DuplicateException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(CommonResponse.of(
				errorCode.getMessage(),
				errorCode.name()
			));
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public final ResponseEntity<CommonResponse> handleUnAuthorizedException(final UnAuthorizedException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(CommonResponse.of(
				errorCode.getMessage(),
				errorCode.name()
			));
	}

	@ExceptionHandler(JsonParsingException.class)
	public final ResponseEntity<CommonResponse> handleUnAuthorizedException(final JsonParsingException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(CommonResponse.of(
						errorCode.getMessage(),
						errorCode.name()
				));
	}
}
