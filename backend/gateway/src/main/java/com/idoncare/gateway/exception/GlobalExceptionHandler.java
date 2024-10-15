package com.idoncare.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuthorizedException.class)
	public final ResponseEntity<CommonResponse> handleAuthorizedException(final AuthorizedException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(CommonResponse.of(
				errorCode.getMessage(),
				errorCode.name()
			));
	}
}
