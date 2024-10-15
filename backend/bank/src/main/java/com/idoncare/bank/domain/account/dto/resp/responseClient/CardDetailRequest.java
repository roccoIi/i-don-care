package com.idoncare.bank.domain.account.dto.resp.responseClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.requestServer.CardDetail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetailRequest {

	@JsonProperty("Header")
	private RequestHeader header;
	private String cardNo;
	private String cvc;
	private String startDate;
	private String endDate;

	public static CardDetailRequest from(RequestHeader header, CardDetail detail) {
		return CardDetailRequest.builder()
			.header(header)
			.cardNo(detail.getCardNo())
			.cvc(detail.getCvc())
			.startDate(detail.getStartDate())
			.endDate(detail.getEndDate())
			.build();
	}

}
