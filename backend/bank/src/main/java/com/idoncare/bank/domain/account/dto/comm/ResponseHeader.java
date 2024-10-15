package com.idoncare.bank.domain.account.dto.comm;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHeader {
	private String responseCode;
	private String responseMessage;
	private String apiName;
	private String transmissionDate;
	private String transmissionTime;
	private String institutionCode;
	private String apiKey;
	private String apiServiceCode;
	private String institutionTransactionUniqueNo;
}
