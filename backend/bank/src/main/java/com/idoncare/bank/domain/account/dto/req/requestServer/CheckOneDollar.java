package com.idoncare.bank.domain.account.dto.req.requestServer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountAuth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckOneDollar {

	@JsonProperty("Header")
	private RequestHeader requestHeader;
	private String accountNo;
	private String authText;
	private String authCode;

	public static CheckOneDollar of(RequestHeader requestHeader, AccountAuth accountAuth) {
		return CheckOneDollar.builder()
			.requestHeader(requestHeader)
			.accountNo(accountAuth.getAccountNum())
			.authText("SSAFY")
			.authCode(accountAuth.getAuthNum())
			.build();
	}
}
