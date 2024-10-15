package com.idoncare.bank.domain.account.dto.resp.responseServer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.ResponseHeader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryList {

	@JsonProperty("Header")
	private ResponseHeader header;

	@JsonProperty("REC")
	private REC rec;


	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class REC {
		private String totalCount;
		private List<History> list = new ArrayList<>();

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class History {
			private Long transactionUniqueNo;
			private String transactionDate;
			private String transactionTime;
			private String transactionType;
			private String transactionTypeName;
			private String transactionAccountNo;
			private Long transactionBalance;
			private Long transactionAfterBalance;
			private String transactionSummary;
			private String transactionMemo;
		}
	}
}
