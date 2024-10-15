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
public class SavingsRegistNotiReq {

	private Long relationId;

	@Override
	public String toString() {
		return "SavingsRegistNotiReq{" +
			"relationId=" + relationId +
			'}';
	}
}
