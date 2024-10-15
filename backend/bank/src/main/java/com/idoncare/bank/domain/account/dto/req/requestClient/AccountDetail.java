package com.idoncare.bank.domain.account.dto.req.requestClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class AccountDetail {

	@JsonProperty("Header")
	private RequestHeader requestHeader;
	private String accountNo;

	public static AccountDetail of(RequestHeader requestHeader, String accountNo) {
		return AccountDetail.builder()
			.requestHeader(requestHeader)
			.accountNo(accountNo)
			.build();
	}
}
