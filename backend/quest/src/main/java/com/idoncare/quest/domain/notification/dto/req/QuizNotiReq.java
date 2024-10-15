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
public class QuizNotiReq {

	private Long relationId;

	private Long amount;

	private Boolean review;

	@Override
	public String toString() {
		return "QuizNotiReq{" +
			"relationId=" + relationId +
			", amount=" + amount +
			", review=" + review +
			'}';
	}
}
