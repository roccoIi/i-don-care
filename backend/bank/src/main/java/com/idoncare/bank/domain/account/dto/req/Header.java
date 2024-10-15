package com.idoncare.bank.domain.account.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Header {
	@JsonProperty("Header")
	public RequestHeader requestHeader;

	public static Header from(RequestHeader requestHeader) {
		return Header.builder()
			.requestHeader(requestHeader)
			.build();
	}
}
