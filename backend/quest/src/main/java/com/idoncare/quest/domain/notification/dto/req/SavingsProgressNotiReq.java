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
public class SavingsProgressNotiReq {

	private Long relationId;

	private Long amount;

	@Override
	public String toString() {
		return "SavingsProgressNotiReq{" +
			"relationId=" + relationId +
			", amount=" + amount +
			'}';
	}
}
