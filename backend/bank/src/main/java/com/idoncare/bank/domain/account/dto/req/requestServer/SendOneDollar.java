package com.idoncare.bank.domain.account.dto.req.requestServer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SendOneDollar {

	@JsonProperty("Header")
	private RequestHeader requestHeader;
	private String accountNo;
	private String authText;

	public static SendOneDollar of(RequestHeader requestHeader, String accountNo) {
		return SendOneDollar.builder()
			.requestHeader(requestHeader)
			.accountNo(accountNo)
			.authText("SSAFY")
			.build();
	}
}
