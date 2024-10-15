package com.idoncare.quest.domain.savings.dto.resp;

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
public class ChildCoinboxDetailResp {
	private Long relationId;
	private Long coinboxId;
	private String goalTitle;
	private Long goalAmount;
	private Long amount;
	private Long interestAmount;

	@Override
	public String toString() {
		return "ChildCoinboxDetailResp{" +
			"relationId=" + relationId +
			", coinboxId=" + coinboxId +
			", goalTitle='" + goalTitle + '\'' +
			", goalAmount=" + goalAmount +
			", amount=" + amount +
			", interestAmount=" + interestAmount +
			'}';
	}
}
