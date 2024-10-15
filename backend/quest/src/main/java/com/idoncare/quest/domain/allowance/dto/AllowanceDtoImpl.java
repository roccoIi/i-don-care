package com.idoncare.quest.domain.allowance.dto;

import java.time.LocalDateTime;

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
public class AllowanceDtoImpl {

	private Long relationId;

	private Long amount;

	private Boolean review;

	public AllowanceDtoImpl(AllowanceDto allowanceDto) {
		relationId = allowanceDto.getRelationId();
		amount = allowanceDto.getAmount();
		this.review = true;
	}

	@Override
	public String toString() {
		return "AllowanceDtoImpl{" +
			"RelationId=" + relationId +
			", Amount=" + amount +
			", review=" + review +
			'}';
	}
}
