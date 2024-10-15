package com.idoncare.bank.domain.account.dto.resp.responseClient;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionHistoryInfo {
	private int count;
	private List<MiniTransactionHistoryInfo> list;


	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class MiniTransactionHistoryInfo {
		private String transactionName;
		private String transactionTypeName;
		private Long cost;
		private Long balance;
		private LocalDateTime createAt;
	}
}
