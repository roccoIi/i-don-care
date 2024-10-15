package com.idoncare.bank.domain.account.dto.resp.responseServer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.ResponseHeader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransfer {

	@JsonProperty("Header")
	private ResponseHeader header;

	@JsonProperty("REC")
	@Builder.Default
	public List<TransResult> rec = new ArrayList<>();


	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TransResult{
		private Long transactionUniqueNo;
		private String accountNo;
		private String transactionDate;
		private String transactionType;
		private String transactionTypeName;
		private String transactionAccountNo;
	}
}
