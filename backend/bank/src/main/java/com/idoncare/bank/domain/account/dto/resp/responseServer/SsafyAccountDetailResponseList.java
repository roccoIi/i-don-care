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
public class SsafyAccountDetailResponseList {

	@JsonProperty("Header")
	private ResponseHeader header;

	@JsonProperty("REC")
	private List<REC> rec = new ArrayList<>();;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class REC {
		private String bankCode;
		private String bankName;
		private String userName;
		private String accountNo;
		private String accountName;
		private String accountTypeCode;
		private String accountTypeName;
		private String accountCreatedDate;
		private String accountExpiryDate;
		private Long dailyTransferLimit;
		private Long oneTimeTransferLimit;
		private Long accountBalance;
		private String lastTransactionDate;
		private String currency;
	}
}
