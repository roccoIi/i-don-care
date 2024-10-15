package com.idoncare.bank.domain.account.dto.req.requestClient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardTransHistory {
	private String cardNo;
	private String cvc;
	private Long merchantId;
	private Long paymentBalance;
}
