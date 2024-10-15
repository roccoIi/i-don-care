package com.idoncare.quest.domain.notification.dto.req;

import java.util.List;

import com.idoncare.quest.domain.allowance.dto.AllowanceDtoImpl;

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
public class AllowanceNotiReq {

	private List<AllowanceDtoImpl> allowanceUpdateList;

	@Override
	public String toString() {
		return "AllowanceNotiReq{" +
			"allowanceUpdateList=" + allowanceUpdateList +
			'}';
	}
}
