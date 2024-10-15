package com.idoncare.user.global.common;

import static com.idoncare.user.global.common.ErrorCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.idoncare.user.global.exception.AuthorizedException;
import com.idoncare.user.global.exception.BadGatewayException;
import com.idoncare.user.global.exception.NotFoundException;

import net.nurigo.sdk.message.exception.NurigoApiKeyException;
import net.nurigo.sdk.message.exception.NurigoBadRequestException;
import net.nurigo.sdk.message.exception.NurigoException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<CommonResponse> handleNotFoundException(final NotFoundException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(CommonResponse.of(
				errorCode.name(),
				errorCode.getMessage()
			));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public final ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(
		final MethodArgumentNotValidException e) {

		String errorMessage = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.findFirst()
			.map(FieldError::getDefaultMessage)
			.orElse("Validation error");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(CommonResponse.of(
				errorMessage,
				G000.name()
			));
	}

	@ExceptionHandler(AuthorizedException.class)
	public final ResponseEntity<CommonResponse> handleAuthorizedException(final AuthorizedException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(CommonResponse.of(
				errorCode.name(),
				errorCode.getMessage()
			));
	}


	@ExceptionHandler({NurigoApiKeyException.class, NurigoBadRequestException.class, NurigoMessageNotReceivedException.class})
	public final ResponseEntity<CommonResponse> handleNurigoException(final NurigoException e) {
		throw new BadGatewayException(A001);
	}

	@ExceptionHandler(BadGatewayException.class)
	public final ResponseEntity<CommonResponse> handleBadGatewayException(final BadGatewayException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
			.body(CommonResponse.of(
				errorCode.name(),
				errorCode.getMessage()
			));
	}

}
