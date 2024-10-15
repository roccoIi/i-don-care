package com.idoncare.bank.domain.account.dto.req.requestServer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.requestClient.CardTransHistory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardTransHistoryRequest {

	@JsonProperty("Header")
	private RequestHeader requestHeader;
	private String cardNo;
	private String cvc;
	private Long merchantId;
	private Long paymentBalance;

	public static CardTransHistoryRequest of(RequestHeader request, CardTransHistory cardTransHistory){
		return CardTransHistoryRequest.builder()
			.requestHeader(request)
			.cardNo(cardTransHistory.getCardNo())
			.cvc(cardTransHistory.getCvc())
			.merchantId(cardTransHistory.getMerchantId())
			.paymentBalance(cardTransHistory.getPaymentBalance())
			.build();
	}
}
