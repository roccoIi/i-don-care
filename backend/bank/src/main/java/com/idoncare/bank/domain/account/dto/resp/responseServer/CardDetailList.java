package com.idoncare.bank.domain.account.dto.resp.responseServer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.ResponseHeader;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetailList {

	@JsonProperty("Header")
	private ResponseHeader responseHeader;

	@JsonProperty("REC")
	@Builder.Default
	private List<REC> rec = new ArrayList<>();

	@Data
	@Builder
	public static class REC{
		private String cardNo;
		private String cvc;
		private String cardUniqueNo;
		private String cardIssuerCode;
		private String cardIssuerName;
		private String cardName;
		private Long baselinePerformance;
		private Long maxBenefitLimit;
		private String cardDescription;
		private String cardExpiryDate;
		private String withdrawalAccountNo;
		private String withdrawalDate;
	}
}
