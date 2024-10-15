package com.idoncare.bank.domain.account.dto.resp.responseServer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.ResponseHeader;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardTransResult {
	@JsonProperty("Header")
	private ResponseHeader header;

	@JsonProperty("REC")
	private Result result;

	@Data
	@Builder
	public static class Result {
		private Long transactionUniqueNo;
		private String categoryId;
		private String categoryName;
		private Long merchantId;
		private String merchantName;
		private String transactionDate;
		private String transactionTime;
		private Long paymentBalance;
	}
}
