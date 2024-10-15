package com.idoncare.bank.domain.account.dto.comm;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RequestHeader {
	private String apiName;
	private String transmissionDate;
	private String transmissionTime;
	private String institutionCode;
	private String fintechAppNo;
	private String apiServiceCode;
	private String institutionTransactionUniqueNo;
	private String apiKey;
	private String userKey;

	public static RequestHeader of(String apiName, String transmissionDate, String transmissionTime,
		String institutionTransactionUniqueNo, String apiKey){

		return RequestHeader.builder()
			.apiName(apiName)
			.transmissionDate(transmissionDate)
			.transmissionTime(transmissionTime)
			.institutionCode("00100")
			.fintechAppNo("001")
			.apiServiceCode(apiName)
			.institutionTransactionUniqueNo(institutionTransactionUniqueNo)
			.apiKey(apiKey)
			.build();
	}

	public static RequestHeader of(String apiName, String transmissionDate, String transmissionTime,
		String institutionTransactionUniqueNo, String apiKey, String userKey){

		return RequestHeader.builder()
			.apiName(apiName)
			.transmissionDate(transmissionDate)
			.transmissionTime(transmissionTime)
			.institutionCode("00100")
			.fintechAppNo("001")
			.apiServiceCode(apiName)
			.institutionTransactionUniqueNo(institutionTransactionUniqueNo)
			.apiKey(apiKey)
			.userKey(userKey)
			.build();
	}

}
