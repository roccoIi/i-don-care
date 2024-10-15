package com.idoncare.bank.domain.account.repository.openFeign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.Header;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountDetail;
import com.idoncare.bank.domain.account.dto.req.requestServer.CardTransHistoryRequest;
import com.idoncare.bank.domain.account.dto.req.requestServer.CheckOneDollar;
import com.idoncare.bank.domain.account.dto.req.requestServer.SendOneDollar;
import com.idoncare.bank.domain.account.dto.req.requestServer.TransactionHistoryListRequest;
import com.idoncare.bank.domain.account.dto.req.requestServer.TransactionHistoryRequest;
import com.idoncare.bank.domain.account.dto.req.requestServer.TransactionRequest;
import com.idoncare.bank.domain.account.dto.resp.responseClient.CardDetailRequest;
import com.idoncare.bank.global.config.FeignConfiguration;

@FeignClient(name = "ssafyApi", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu", configuration = FeignConfiguration.class)
public interface BankOpenFeign {

	// 1원 인증 송금
	@PostMapping("/accountAuth/openAccountAuth")
	Map<String, Map<String, String>> sendAccountAuthNum (@RequestBody SendOneDollar sendOneDollar);

	// 1원 인증 검증
	@PostMapping("/accountAuth/checkAuthCode")
	Map<String, Object> checkAccountAuthNum(@RequestBody CheckOneDollar checkOneDollar);

	// 계좌 조회 (단건)
	@PostMapping("/demandDeposit/inquireDemandDepositAccount")
	String getAccountDetail(@RequestBody AccountDetail accountDetail);

	// 20240701 이후 거래내역 조회
	@PostMapping("/demandDeposit/inquireTransactionHistoryList")
	String getTransactionHistory(@RequestBody TransactionHistoryListRequest transactionHistoryListRequest);

	// 거래내역 조회 (단건)
	@PostMapping("demandDeposit/inquireTransactionHistory")
	String getOneTransactionHistory(@RequestBody TransactionHistoryRequest transactionHistoryRequest);

	// 계좌이체
	@PostMapping("/demandDeposit/updateDemandDepositAccountTransfer")
	String sendMoney(@RequestBody TransactionRequest transactionRequest);

	// [카드] 거래내역 생성
	@PostMapping("/creditCard/createCreditCardTransaction")
	String createCreditCardTransaction(@RequestBody CardTransHistoryRequest cardTransHistoryRequest);

	// [계좌] 전체 생성된 계좌 조회
	@PostMapping("/demandDeposit/inquireDemandDepositAccountList")
	String inquireDemandDepositAccountList(@RequestBody Header header);

	// [카드] 거래내역 조회
	@PostMapping("/creditCard/inquireCreditCardTransactionList")
	String getCreditCardTransactionList(@RequestBody CardDetailRequest cardDetailRequest);

	// [카드] 카드 목록 조회
	@PostMapping("/creditCard/inquireSignUpCreditCardList")
	String getCreditCardList(@RequestBody Header header);
 }
