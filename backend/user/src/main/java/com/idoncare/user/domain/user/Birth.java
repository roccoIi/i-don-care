package com.idoncare.user.domain.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class Birth {
	String birth;

	public Birth(String birth) {
		this.birth = birth;
	}

	public static LocalDate toBirth(String birthAndGender) {
		String birth = birthAndGender.substring(0, 6);
		int year = Integer.parseInt(birth.substring(0, 2)); // 연도 추출 (YY)
		int currentYear = LocalDate.now().getYear() % 100; // 현재 연도의 마지막 두 자리

		if (year <= currentYear) {
			year += 2000;
		} else {
			year += 1900;
		}

		String finalBirth = String.format("%04d%s", year, birth.substring(2));
		return LocalDate.parse(finalBirth, DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
}
