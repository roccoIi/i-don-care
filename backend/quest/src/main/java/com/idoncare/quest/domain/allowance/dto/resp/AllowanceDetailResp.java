package com.idoncare.quest.domain.allowance.dto.resp;

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
public class AllowanceDetailResp {
	private Long allowanceId;
	private Long amount;
	private Long type;
	private Long day;
	private Long relationId;
}
