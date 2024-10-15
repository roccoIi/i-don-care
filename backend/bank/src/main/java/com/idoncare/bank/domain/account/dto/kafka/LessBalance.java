package com.idoncare.bank.domain.account.dto.kafka;

import com.idoncare.bank.domain.account.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessBalance {
	private Long userId;
	private Long transferAmount;
	private Long currentBalance;
	private Long amountDeficit;

	public static LessBalance toEntity(Account account, Long transferAmount, Long amountDeficit){
		return LessBalance.builder()
			.userId(account.getUserId())
			.transferAmount(transferAmount)
			.currentBalance(account.getBalance())
			.amountDeficit(amountDeficit)
			.build();
	}

	public static LessBalance toEntity(Long userId, Long transferAmount, Long currentBalance, Long amountDeficit){
		return LessBalance.builder()
				.userId(userId)
				.transferAmount(transferAmount)
				.currentBalance(currentBalance)
				.amountDeficit(amountDeficit)
				.build();
	}
}
