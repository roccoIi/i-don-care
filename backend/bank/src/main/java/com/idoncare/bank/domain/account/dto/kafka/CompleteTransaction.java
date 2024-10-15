package com.idoncare.bank.domain.account.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompleteTransaction {
	private String accountNumber;
	private Long uniqueNumber;

	public static CompleteTransaction toEntity(String accountNumber, Long uniqueNumber) {
		return CompleteTransaction.builder()
			.accountNumber(accountNumber)
			.uniqueNumber(uniqueNumber)
			.build();
	}
}
