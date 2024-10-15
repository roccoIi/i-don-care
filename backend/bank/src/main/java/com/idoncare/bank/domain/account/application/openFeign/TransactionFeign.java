package com.idoncare.bank.domain.account.application.openFeign;

import static com.idoncare.bank.global.common.ErrorCode.*;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.Header;
import com.idoncare.bank.domain.account.dto.req.requestClient.CardTransHistory;
import com.idoncare.bank.domain.account.dto.req.requestClient.SendInfo;
import com.idoncare.bank.domain.account.dto.req.requestServer.CardDetail;
import com.idoncare.bank.domain.account.dto.req.requestServer.CardTransHistoryRequest;
import com.idoncare.bank.domain.account.dto.req.requestServer.TransactionHistoryListRequest;
import com.idoncare.bank.domain.account.dto.req.requestServer.TransactionHistoryRequest;
import com.idoncare.bank.domain.account.dto.req.requestServer.TransactionRequest;
import com.idoncare.bank.domain.account.dto.resp.responseClient.CardDetailRequest;
import com.idoncare.bank.domain.account.dto.resp.responseServer.AccountTransfer;
import com.idoncare.bank.domain.account.dto.resp.responseServer.CardDetailList;
import com.idoncare.bank.domain.account.dto.resp.responseServer.CardTransHistoryList;
import com.idoncare.bank.domain.account.dto.resp.responseServer.CardTransResult;
import com.idoncare.bank.domain.account.dto.resp.responseServer.TransactionHistory;
import com.idoncare.bank.domain.account.dto.resp.responseServer.TransactionHistoryList;
import com.idoncare.bank.domain.account.repository.openFeign.BankOpenFeign;
import com.idoncare.bank.global.exception.JsonParsingException;
import com.idoncare.bank.global.util.HeaderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionFeign {

	private final HeaderUtil headerUtil;
	private final BankOpenFeign bankOpenFeign;
	private final ObjectMapper objectMapper;

	/**
	 * 특정 계좌의 20240701 ~ 현재까지의 거래내역을 모두 가져온다.
	 * @param accountNumber 계좌번호
	 * @return TransactionHistoryList
	 */
	public TransactionHistoryList getTransactionHistoryList(String accountNumber) {
		// Header
		RequestHeader header = headerUtil.makeHeadWithUserKey("inquireTransactionHistoryList");

		// openFeign
		String responseJson = bankOpenFeign.getTransactionHistory(
			TransactionHistoryListRequest.ofNow(header, accountNumber, headerUtil.nowDate()));

		// return 값
		TransactionHistoryList responseDto;
		try{
			responseDto = objectMapper.readValue(responseJson, TransactionHistoryList.class);
		} catch (JsonProcessingException e) {
			throw new JsonParsingException(G003);
		}

		return responseDto;
	}

	/**
	 * 특정 계좌의 거래내역 중 거래번호와 일치하는 거래내역을 1건 반환한다.
	 * @param accountNum 계좌번호
	 * @param transactionUniqueNo 교유 거래번호
	 * @return TransactionHistory
	 */
	public TransactionHistory getTransactionHistoryDetail(String accountNum, Long transactionUniqueNo) {
		// Header
		RequestHeader header = headerUtil.makeHeadWithUserKey("inquireTransactionHistory");

		// openFeign
		String responseJson = bankOpenFeign.getOneTransactionHistory(new TransactionHistoryRequest(header, accountNum, transactionUniqueNo));

		// return 값
		TransactionHistory responseDto;
		try{
			responseDto = objectMapper.readValue(responseJson, TransactionHistory.class);
		} catch (JsonProcessingException e) {
			throw new JsonParsingException(G003);
		}

		return responseDto;
	}

	/**
	 * 송금 진행
	 * @param sendInfo (보내는 계좌, 받는 계좌, 금액)
	 * @return AccountTransfer
	 */
	public AccountTransfer sendMoney (SendInfo sendInfo) {
		// Header
		RequestHeader header = headerUtil.makeHeadWithUserKey("updateDemandDepositAccountTransfer");

		// openFeign
		String responseJson = bankOpenFeign.sendMoney(TransactionRequest.toEntity(header, sendInfo));

		// return 값
		AccountTransfer responseDto;
		try {
			responseDto = objectMapper.readValue(responseJson, AccountTransfer.class);
		} catch (JsonProcessingException e) {
			throw new JsonParsingException(G003);
		}

		return responseDto;
	}

	/**
	 * 카드 거래내역 생성
 	 * @param cardTransHistory
	 * @return
	 */
	public CardTransResult makeCardTransHistory (CardTransHistory cardTransHistory) {
		// Header
		RequestHeader header = headerUtil.makeHeadWithUserKey("createCreditCardTransaction");

		// openFeign
		String responseJson = bankOpenFeign.createCreditCardTransaction(
			CardTransHistoryRequest.of(header, cardTransHistory));

		// return 값
		CardTransResult responseDto;
		try {
			responseDto = objectMapper.readValue(responseJson, CardTransResult.class);
		} catch (JsonProcessingException e) {
			throw new JsonParsingException(G003);
		}

		return responseDto;
	}

	/**
	 * 해당 카드의 전체 거래내역 조회
	 * @param cardDetail
	 * @return
	 */
	public CardTransHistoryList getCardTransHistoryList(CardDetail cardDetail) {

		// Header
		RequestHeader header = headerUtil.makeHeadWithUserKey("inquireCreditCardTransactionList");

		// openFeign
		String responseJson = bankOpenFeign.getCreditCardTransactionList(
			CardDetailRequest.from(header, cardDetail));

		// return 값
		CardTransHistoryList responseDto;
		try {
			responseDto = objectMapper.readValue(responseJson, CardTransHistoryList.class);
		} catch (JsonProcessingException e) {
			throw new JsonParsingException(G003);
		}

		return responseDto;
	}

	/**
	 * 현재 DB에 저장된 전체 카드목록 반환
	 * @return
	 */
	public CardDetailList getCreditCardList() {

		// Header
		RequestHeader header = headerUtil.makeHeadWithUserKey("inquireSignUpCreditCardList");

		// openFeign
		String responseJson = bankOpenFeign.getCreditCardList(Header.from(header));

		// return 값
		CardDetailList responseDto;
		try {
			responseDto = objectMapper.readValue(responseJson, CardDetailList.class);
		} catch (JsonProcessingException e) {
			throw new JsonParsingException(G003);
		}

		return responseDto;
	}
}
