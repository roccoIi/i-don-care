package com.idoncare.quest.domain.allowance.dto.req;

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
public class AllowanceReq {
	@PositiveOrZero
	@NotNull
	private Long relationId;
	@PositiveOrZero
	@NotNull
	private Long type;
	@PositiveOrZero
	@NotNull
	private Long day;
	@PositiveOrZero
	@NotNull
	private Long amount;
}
