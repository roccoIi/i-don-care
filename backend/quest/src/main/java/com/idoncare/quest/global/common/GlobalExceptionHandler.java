package com.idoncare.quest.global.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.idoncare.quest.global.exception.DuplicateException;
import com.idoncare.quest.global.exception.NotFoundException;
import com.idoncare.quest.global.exception.UnAuthorizedException;

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

	@ExceptionHandler(DuplicateException.class)
	public final ResponseEntity<CommonResponse> handleDuplicateException(final DuplicateException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(CommonResponse.of(
				errorCode.getMessage(),
				errorCode.name()
			));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public final ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(
		final MethodArgumentNotValidException e) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(CommonResponse.of(
				ErrorCode.Q002.getMessage(),
				ErrorCode.Q002.name()
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

}
