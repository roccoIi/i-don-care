package com.idoncare.bank.domain.account.dto.resp.responseClient;

import com.idoncare.bank.domain.account.dto.resp.responseServer.SsafyAccountDetailResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAccountDetail {
	private String bankName; //은행명
	private String accountNo; //계좌번호
	private String accountName; //상품명
	private String accountTypeName; //상품종류명
	private String accountCreateDate; //계좌개설일
	private String accountExpireDate; //계좌만기일
	private Long dailyTransferLimit; //1일이체한도
	private Long oneTimeTransferLimit; //1회이체한도
	private Long accountBalance; //계좌잔액
	private String lastTransactionDate; //최종거래일

	public static UserAccountDetail of(SsafyAccountDetailResponse ssafyAccountDetailResponse){
		return UserAccountDetail.builder()
			.bankName(ssafyAccountDetailResponse.getRec().getBankName())
			.accountNo(ssafyAccountDetailResponse.getRec().getAccountNo())
			.accountName(ssafyAccountDetailResponse.getRec().getAccountName())
			.accountTypeName(ssafyAccountDetailResponse.getRec().getAccountTypeName())
			.accountCreateDate(ssafyAccountDetailResponse.getRec().getAccountCreatedDate())
			.accountExpireDate(ssafyAccountDetailResponse.getRec().getAccountExpiryDate())
			.dailyTransferLimit(ssafyAccountDetailResponse.getRec().getDailyTransferLimit())
			.oneTimeTransferLimit(ssafyAccountDetailResponse.getRec().getOneTimeTransferLimit())
			.accountBalance(ssafyAccountDetailResponse.getRec().getAccountBalance())
			.lastTransactionDate(ssafyAccountDetailResponse.getRec().getLastTransactionDate())
			.build();
	}
}
