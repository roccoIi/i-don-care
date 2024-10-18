package com.idoncare.user.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDto {
	Long userId;
	Long transferAmount;
	Long currentBalance;
	Long amountDeficit;
}
