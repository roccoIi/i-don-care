package com.idoncare.user.domain.enums;

import lombok.Getter;

@Getter
public enum GenderType {
	MALE("Male"),
	FEMALE("Female"),
	OTHER("Other");

	private final String description;

	GenderType(String description) {
		this.description = description;
	}

	public static GenderType toGender(String birthAndGender) {
		char genderCode = birthAndGender.charAt(6);

		return switch (genderCode) {
			case '1', '3' -> MALE;
			case '2', '4' -> FEMALE;
			default -> OTHER; // 그 외의 숫자는 OTHER로 판별
		};
	}
}
