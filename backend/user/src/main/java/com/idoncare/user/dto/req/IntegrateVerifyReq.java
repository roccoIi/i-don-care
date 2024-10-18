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
public class IntegrateVerifyReq {
	@NotBlank(message = "전화번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^(010|011)\\d{7,8}$", message = "유효한 전화번호 형식이 아닙니다.")
	private String tel;

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^[0-9]{6}$", message = "비밀번호는 숫자로 이루어진 6자리여야 합니다.")
	private String code;
}
