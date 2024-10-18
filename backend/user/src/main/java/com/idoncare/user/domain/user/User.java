package com.idoncare.user.domain.user;

import java.time.LocalDate;

import com.idoncare.user.domain.enums.GenderType;
import com.idoncare.user.domain.enums.RoleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

	private Long userId;
	private Long accountId;
	private String userName;
	private String userPhone;
	private String password;
	private LocalDate birth;
	private GenderType genderType;
	private RoleType roleType;
	private Boolean relationYn;

	public void updatePassword(
		@NotBlank(message = "비밀번호는 필수 입력 값입니다.") @Pattern(regexp = "^[0-9]{6}$", message = "비밀번호는 숫자로 이루어진 6자리여야 합니다.") String password) {
		this.password = password;
	}
}
