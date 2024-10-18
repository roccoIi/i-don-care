package com.idoncare.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegrateSignUpReq {
	@NotBlank(message = "전화번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^(010|011)\\d{7,8}$", message = "유효한 전화번호 형식이 아닙니다.")
	private String tel;

	@NotBlank(message = "생일은 필수 입력 값입니다.")
	@Pattern(regexp = "^(\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[1-4]$",
		message = "생일은 YYMMDD와 성별(1, 2, 3, 4) 형식이어야 합니다.")
	private String birth;

	private String userName;
}
