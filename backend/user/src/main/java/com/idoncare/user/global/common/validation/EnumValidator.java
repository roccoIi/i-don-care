package com.idoncare.user.global.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<EnumValid, String> {

	private java.lang.Enum<?>[] enumConstants;

	@Override
	public void initialize(EnumValid annotation) {
		enumConstants = annotation.enumClass().getEnumConstants();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;  // null인 경우는 다른 검증으로 처리될 수 있으므로, 여기서는 통과
		}
		return Arrays.stream(enumConstants)
			.anyMatch(e -> e.name().equals(value));
	}
}
