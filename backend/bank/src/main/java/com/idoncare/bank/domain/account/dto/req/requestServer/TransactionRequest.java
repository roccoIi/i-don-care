package com.idoncare.bank.domain.account.dto.req.requestServer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.requestClient.SendInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionRequest {

	@JsonProperty("Header")
	private RequestHeader requestHeader;
	private String depositAccountNo; // 입금계좌번호
	private Long transactionBalance; // 거래금액
	private String withdrawalAccountNo; // 출금계좌번호
	private String depositTransactionSummary; // 거래 요약내용(입금계좌)
	private String withdrawalTransactionSummary; // 거래 요약내용(출금계좌)

	public static TransactionRequest toEntity(RequestHeader requestHeader, SendInfo sendInfo) {
		return TransactionRequest.builder()
			.requestHeader(requestHeader)
			.depositAccountNo(sendInfo.getReceiveAccountNum())
			.transactionBalance(sendInfo.getAmount())
			.withdrawalAccountNo(sendInfo.getSendAccountNum())
			//TODO: 이벤트 발생시켜서 여기에 이벤트별로 넣을 수 있을지 확인할 필요가 있다.
			.build();
	}
}
