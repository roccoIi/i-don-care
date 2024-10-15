package com.idoncare.bank.domain.account.dto.req.requestServer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.requestClient.TransactionQuery;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TransactionHistoryListRequest {

	@JsonProperty("Header")
	private RequestHeader requestHeader;
	private String accountNo;
	private String startDate;
	private String endDate;
	private String transactionType;
	private String orderByType;

	public static TransactionHistoryListRequest of(RequestHeader requestHeader, TransactionQuery transactionQuery) {
		return TransactionHistoryListRequest.builder()
			.requestHeader(requestHeader)
			.accountNo(transactionQuery.getAccountNo())
			.startDate(transactionQuery.getStartDate())
			.endDate(transactionQuery.getEndDate())
			.transactionType(transactionQuery.getTransactionType())
			.orderByType(transactionQuery.getOrderByType())
			.build();
	}

	public static TransactionHistoryListRequest ofNow(RequestHeader requestHeader, String accountNo, String endDate) {
		return TransactionHistoryListRequest.builder()
			.requestHeader(requestHeader)
			.accountNo(accountNo)
			.startDate("20240701")
			.endDate(endDate)
			.transactionType("A")
			.orderByType("DESC")
			.build();
	}

}
