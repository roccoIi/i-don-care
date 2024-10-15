package com.idoncare.bank.domain.account.dto.resp.responseServer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.ResponseHeader;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class CardTransHistoryList {
	@JsonProperty("Header")
	private ResponseHeader header;

	@JsonProperty("REC")
	private REC result;

	@Data
	@Builder
	public static class REC{
		private String cardIssuerCode;
		private String cardIssuerName;
		private String cardName;
		private String cardNo;
		private String cvc;
		private Long estimatedBalance;
		@Builder.Default
		private List<TransactionList> transactionList = new ArrayList<>();

		@Data
		@Builder
		public static class TransactionList{
			private Long transactionUniqueNo;
			private String categoryId;
			private String categoryName;
			private Long merchantId;
			private String merchantName;
			private String transactionDate;
			private String transactionTime;
			private Long transactionBalance;
			private String cardStatus;
			private String billStatementsYn;
			private String billStatementsStatus;
		}
	}
}
