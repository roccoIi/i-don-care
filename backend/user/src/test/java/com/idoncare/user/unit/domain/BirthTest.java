package com.idoncare.user.unit.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.idoncare.user.domain.user.Birth;
import com.idoncare.user.domain.enums.GenderType;

public class BirthTest {

	@Test
	@DisplayName("생년월일과 성별 추출 테스트")
	void testBirthAndGenderExtraction() {
		// Given
		String birthAndGenderInput = "9801082";

		// When
		Birth birth = new Birth(birthAndGenderInput);
		LocalDate localDate = Birth.toBirth(birthAndGenderInput);
		GenderType genderType = GenderType.toGender(birthAndGenderInput);

		// Then
		assertThat(localDate).isEqualTo(LocalDate.of(1998, 1, 8));
		assertThat(genderType).isEqualTo(GenderType.FEMALE);
	}
}
