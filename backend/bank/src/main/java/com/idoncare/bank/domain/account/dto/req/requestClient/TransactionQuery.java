package com.idoncare.bank.domain.account.dto.req.requestClient;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionQuery {
	private String accountNo;
	private String startDate;
	private String endDate;
	private String transactionType;
	private String orderByType;
}
