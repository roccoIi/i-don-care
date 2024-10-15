package com.idoncare.bank.domain.account.dto.resp.responseClient;

import com.idoncare.bank.domain.account.dto.resp.responseServer.SsafyAccountDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentTransactionPeople {
	private String userName;
	private String accountNum;
	private String bankName;


	public static RecentTransactionPeople of(SsafyAccountDetailResponse ssafyAccountDetailResponse) {
		return RecentTransactionPeople.builder()
				.accountNum(ssafyAccountDetailResponse.getRec().getAccountNo())
				.bankName(ssafyAccountDetailResponse.getRec().getBankName())
				.build();
	}
}
