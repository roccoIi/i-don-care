package com.idoncare.bank.domain.account.dto.resp.responseClient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceInfo {
	private Long balance;
}
