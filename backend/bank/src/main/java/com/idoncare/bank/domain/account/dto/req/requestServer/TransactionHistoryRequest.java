package com.idoncare.bank.domain.account.dto.req.requestServer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TransactionHistoryRequest {

	@JsonProperty("Header")
	private RequestHeader header;
	private String accountNo;
	private Long transactionUniqueNo;
}
