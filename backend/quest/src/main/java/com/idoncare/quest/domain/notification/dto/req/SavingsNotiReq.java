package com.idoncare.quest.domain.notification.dto.req;

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
public class SavingsNotiReq {

	private Long relationId;

	private Long amount;

	private Long interestAmount;

	private boolean review;

	@Override
	public String toString() {
		return "SavingsNotiReq{" +
			"relationId=" + relationId +
			", amount=" + amount +
			", interestAmount=" + interestAmount +
			", review=" + review +
			'}';
	}
}
