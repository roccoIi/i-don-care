package com.idoncare.quest.domain.savings.dto.req;

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
public class AddAmountReq {
	@PositiveOrZero
	@NotNull
	private Long coinboxId;
	@PositiveOrZero
	@NotNull
	private Long addAmount;
}
