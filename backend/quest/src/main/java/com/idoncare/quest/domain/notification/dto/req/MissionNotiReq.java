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
public class MissionNotiReq {

	private Long relationId;

	private String title;

	private Long amount;

	private Boolean review;

	@Override
	public String toString() {
		return "MissionNotiReq{" +
			"relationId=" + relationId +
			", title='" + title + '\'' +
			", amount=" + amount +
			", review=" + review +
			'}';
	}
}
