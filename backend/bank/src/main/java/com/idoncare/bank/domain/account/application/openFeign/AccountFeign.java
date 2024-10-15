package com.idoncare.bank.domain.account.application.openFeign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idoncare.bank.domain.account.dto.req.Header;
import com.idoncare.bank.domain.account.dto.resp.responseServer.SsafyAccountDetailResponseList;
import com.idoncare.bank.global.common.ErrorCode;
import com.idoncare.bank.global.exception.JsonParsingException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountDetail;
import com.idoncare.bank.domain.account.dto.resp.responseServer.SsafyAccountDetailResponse;
import com.idoncare.bank.domain.account.repository.openFeign.BankOpenFeign;
import com.idoncare.bank.global.util.HeaderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountFeign {

	private final HeaderUtil headerUtil;
	private final BankOpenFeign bankOpenFeign;
	private final ObjectMapper objectMapper;

	/**
	 * 계좌번호(단건) 에 대한 상세정보를 가져온다.
	 * @param accountNumber 계좌번호
	 * @return SsafyAccountDetailResponse
	 */
	public SsafyAccountDetailResponse getOneAccountDetail(String accountNumber) {
		// Header
		RequestHeader requestHeaderAccount = headerUtil.makeHeadWithUserKey("inquireDemandDepositAccount");

		// openFeign
		String responseJson = bankOpenFeign.getAccountDetail(new AccountDetail(requestHeaderAccount, accountNumber));

		// return 값
		SsafyAccountDetailResponse responseDto;
		try{
			responseDto = objectMapper.readValue(responseJson, SsafyAccountDetailResponse.class);
		} catch (JsonProcessingException e) {
			throw new JsonParsingException(ErrorCode.G003);
		}

		return responseDto;
	}

	/**
	 * 현재 SSAFY DB에 등록된 계좌 정보를 반환합니다.
	 * @return
	 */
	public SsafyAccountDetailResponseList registerAccount() {
		// Header
		RequestHeader requestHeader = headerUtil.makeHeadWithUserKey("inquireDemandDepositAccountList");

		System.out.println(requestHeader);
		// openFeign
		String responseJson = bankOpenFeign.inquireDemandDepositAccountList(Header.from(requestHeader));

		// return 값
		SsafyAccountDetailResponseList responseDto;
		try{
			responseDto = objectMapper.readValue(responseJson, SsafyAccountDetailResponseList.class);
		} catch (JsonProcessingException e) {
			throw new JsonParsingException(ErrorCode.G003);
		}

		return responseDto;

	}


}
