package com.idoncare.quest.domain.savings.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class CoinboxReq {
	@PositiveOrZero
	@NotNull
	private Long relationId;
	@NotBlank(message = "저금통의 목표는 필수 입력 값입니다.")
	@NotNull
	private String goalTitle;
	@PositiveOrZero
	@NotNull
	private Long goalAmount;

	@Override
	public String toString() {
		return "SavingsReq{" +
			"relationId=" + relationId +
			", goalTitle='" + goalTitle + '\'' +
			", goalAmount=" + goalAmount +
			'}';
	}
}
