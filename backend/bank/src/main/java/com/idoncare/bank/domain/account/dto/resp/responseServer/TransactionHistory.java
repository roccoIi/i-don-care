package com.idoncare.bank.domain.account.dto.resp.responseServer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.ResponseHeader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistory {

	@JsonProperty("Header")
	private ResponseHeader header;

	@JsonProperty("REC")
	private TransactionHistoryList.REC.History rec;
}
