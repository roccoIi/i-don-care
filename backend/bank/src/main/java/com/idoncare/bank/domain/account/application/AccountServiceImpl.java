package com.idoncare.bank.domain.account.application;

import static com.idoncare.bank.global.common.ErrorCode.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.bank.domain.account.application.kafka.KafkaProducer;
import com.idoncare.bank.domain.account.application.openFeign.AccountFeign;
import com.idoncare.bank.domain.account.application.openFeign.TransactionFeign;
import com.idoncare.bank.domain.account.application.openFeign.UserFeign;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountAuth;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountNumber;
import com.idoncare.bank.domain.account.dto.req.requestClient.CardTransHistory;
import com.idoncare.bank.domain.account.dto.req.requestServer.CardDetail;
import com.idoncare.bank.domain.account.dto.req.requestServer.CheckOneDollar;
import com.idoncare.bank.domain.account.dto.req.requestServer.SendOneDollar;
import com.idoncare.bank.domain.account.dto.resp.responseClient.AuthNumResponse;
import com.idoncare.bank.domain.account.dto.resp.responseClient.BalanceInfo;
import com.idoncare.bank.domain.account.dto.resp.responseClient.UserAccountDetail;
import com.idoncare.bank.domain.account.dto.resp.responseClient.UserInfoDto;
import com.idoncare.bank.domain.account.dto.resp.responseServer.CardDetailList;
import com.idoncare.bank.domain.account.dto.resp.responseServer.CardTransHistoryList;
import com.idoncare.bank.domain.account.dto.resp.responseServer.SsafyAccountDetailResponse;
import com.idoncare.bank.domain.account.dto.resp.responseServer.SsafyAccountDetailResponseList;
import com.idoncare.bank.domain.account.dto.resp.responseServer.TransactionHistoryList;
import com.idoncare.bank.domain.account.entity.Account;
import com.idoncare.bank.domain.account.entity.CardTrans;
import com.idoncare.bank.domain.account.entity.Trans;
import com.idoncare.bank.domain.account.repository.AccountRepository;
import com.idoncare.bank.domain.account.repository.CardTransRepository;
import com.idoncare.bank.domain.account.repository.TransRepository;
import com.idoncare.bank.domain.account.repository.openFeign.BankOpenFeign;
import com.idoncare.bank.global.exception.DuplicateException;
import com.idoncare.bank.global.exception.NotFoundException;
import com.idoncare.bank.global.util.HeaderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final HeaderUtil headerUtil;
	private final BankOpenFeign bankOpenFeign;
	private final AccountRepository accountRepository;
	private final TransRepository transRepository;
	private final AccountFeign accountFeign;
	private final TransactionFeign transactionFeign;
	private final UserFeign userFeign;
	private final CardTransRepository cardTransRepository;


	/**
	 * 1원 송금을 통한 인증번호 발송
	 * @param accountNum
	 */
	@Override
	@Transactional
	public void sendAuthNum(String accountNum) {
		String url = "openAccountAuth";

		// 기존 계좌 확인 => 만일 이미 DB에 존재해는 계좌라면 오류반환, 아니라면 등록 후 진행
		if(accountRepository.existsByAccountNum(accountNum)) {
			throw new DuplicateException(B001);
		}

		// Header 생성
		RequestHeader requestHeader = headerUtil.makeHeadWithUserKey(url);
		SendOneDollar sendOneDollar = SendOneDollar.of(requestHeader, accountNum);

		bankOpenFeign.sendAccountAuthNum(sendOneDollar);
	}

	/**
	 * 계좌번호와 인증번호를 통해 인증 진행한다.
	 * @param accountAuth
	 */
	@Override
	public void checkAuthNum(AccountAuth accountAuth) {
		String url = "checkAuthCode";

		// Header 생성
		RequestHeader requestHeader = headerUtil.makeHeadWithUserKey(url);
		CheckOneDollar checkOneDollar = CheckOneDollar.of(requestHeader, accountAuth);

		// SSAFY API를 통해 인증진행, 오류가 발생한다면 오류코드 반환
		bankOpenFeign.checkAccountAuthNum(checkOneDollar);
	}

	/**
	 * 계좌번호를 통해 account 객체를 생성한다.
	 * 생성 후 해당 계좌와 연결된 계좌거래내역 & 카드결제내역을 불러온다.
	 * @param accountAuth
	 * @param userId
	 */
	@Override
	@Transactional
	public void registAccount(AccountAuth accountAuth, Long userId) {
		// Openfeign
		SsafyAccountDetailResponse accountDetail = accountFeign.getOneAccountDetail(accountAuth.getAccountNum());
		Account account = Account.toEntity(accountDetail, userId);

		// 이미 계좌가 등록되어있다면 중복게좌 등록으로 오류반환, 그렇지 않다면 등록진행
		if (accountRepository.existsByAccountNum(account.getAccountNum())) {
			throw new DuplicateException(B001);
		} else {
			accountRepository.save(account);
		}

		// 계좌 거래내역 저장
		saveTransactionHistory(account);

		// 카드 거래내역 저장
		saveCardTransactionHistory(account);
	}

	/**
	 * 카드 결제 내역을 저장한다.
	 * @param userId
	 */
	@Override
	public void registCard(Long userId) {
		log.info("===== 유저 아이디로 카드 번호 찾기 시작=====");
		Account account = accountRepository.findByUserId(userId)
			.orElseThrow(() -> new NotFoundException(B004));
		log.info("===== 유저 아이디로 카드 번호 찾기 완료=====");

		// 카드 거래내역 저장
		saveCardTransactionHistory(account);
	}

	/**
	 * 주어진 계좌의 거래 내역을 조회하고 저장
	 * @param account
	 */
	private void saveTransactionHistory(Account account) {
		// Openfeign (계좌 거래내역 저장)
		TransactionHistoryList transactionList = transactionFeign.getTransactionHistoryList(account.getAccountNum());

		// 전체 거래기록을 변환하여 list에 담고 한번 저장
		List<Trans> transList = new ArrayList<>();
		for (int i = 0; i < transactionList.getRec().getList().size(); i++) {
			transList.add(Trans.toEntity(account, transactionList.getRec().getList().get(i)));
		}

		transRepository.saveAll(transList);
	}

	/**
	 * 주어진 계좌에 연결된 카드 거래 내역을 조회하고 저장
	 * @param account
	 */
	private void saveCardTransactionHistory(Account account) {
		// 전체 카드 목록 조회
		CardDetailList cardDetailList = transactionFeign.getCreditCardList();

		// 카드 목록 중 계좌번호가 일치하는 카드를 찾아서 카드 파라미터를 만든다.
		CardDetail detail = null;
		for (int i = 0; i < cardDetailList.getRec().size(); i++) {
			if (cardDetailList.getRec().get(i).getWithdrawalAccountNo().equals(account.getAccountNum())) {
				detail = CardDetail.from(cardDetailList.getRec().get(i).getCardNo(),
					cardDetailList.getRec().get(i).getCvc());
				break;
			}
		}

		System.out.println(detail);

		if (detail == null) return;

		// Openfeign (카드 거래내역 저장)
		CardTransHistoryList cardTransHistoryList = transactionFeign.getCardTransHistoryList(detail);

		System.out.println(cardTransHistoryList);
		// 내가 필요한 정보들만 뽑아서 엔티티 저장할 준비
		List<CardTrans> list = new ArrayList<>();
		if(cardTransHistoryList.getResult().getTransactionList() != null){
			for (int i = 0; i < cardTransHistoryList.getResult().getTransactionList().size(); i++) {
				list.add(CardTrans.toEntity(cardTransHistoryList.getResult().getTransactionList().get(i), account));
			}
		}

		System.out.println(list);

		cardTransRepository.saveAll(list);
	}


	/**
	 * 잔액조회
	 * @param userId
	 * @return
	 */
	@Override
	public BalanceInfo getBalance(Long userId) {
		Long balance = accountRepository.findBalanceByUserId(userId)
			.orElseThrow(() -> new NotFoundException(B004));

		return BalanceInfo.builder()
			.balance(balance)
			.build();
	}

	/**
	 * 해당 유저에게 등록된 계좌 상세내역 반환
	 * @param userId
	 * @return
	 */
	@Override
	public UserAccountDetail getAccountDetail(Long userId) {
		String accountNum = accountRepository.findAccountNumByUserId(userId)
			.orElseThrow(() -> new NotFoundException(B004));

		SsafyAccountDetailResponse accountDetail = accountFeign.getOneAccountDetail(accountNum);

		return UserAccountDetail.of(accountDetail);
	}

	/**
	 * 계좌번호를 이용해서 해당 계좌 소유주를 반환한다.
	 * @param accountNumber
	 * @return
	 */
	@Override
	public String getUserId(AccountNumber accountNumber) {
		Long userId = accountRepository.findUserIdByAccountNum(accountNumber.getAccountNum())
				.orElseThrow(() -> new NotFoundException(B004));

		List<Long> tmpList = new ArrayList<>();
		tmpList.add(userId);

		UserInfoDto userDetail = userFeign.getUserInfoDtoByUserId(tmpList);
		return userDetail.getData().getFirst().getUserName()  == null ? "Undefined" : userDetail.getData().getFirst().getUserName();
	}

	/**
	 * 1원인증시 필요한 인증번호 반환
	 * @param accountNumber
	 * @return
	 */
	@Override
	public AuthNumResponse accountHistoryList(AccountNumber accountNumber) {
		// 거래내역 불러오기
		TransactionHistoryList transactionHistoryList = transactionFeign.getTransactionHistoryList(accountNumber.getAccountNum());

		for(int i = 0; i < transactionHistoryList.getRec().getList().size(); i++){
			String summary = transactionHistoryList.getRec().getList().get(i).getTransactionSummary();
			if(summary.contains("SSAFY")){
				return new AuthNumResponse((summary.split(" ")[1]));
			}
		}

		return new AuthNumResponse("인증번호가 존재하지 않습니다.");
	}

	/**
	 * SSAFY DB에 저장된 계좌 리스트 중 서비스에 등록되지 않은 계좌를 반환한다.
	 * @return
	 */
	@Override
	public List<AccountNumber> unRegisterAccount() {
		SsafyAccountDetailResponseList ssafyAccountDetailResponseList = accountFeign.registerAccount();

		//SSAFY DB에 저장된 계좌 리스트 반환
		Queue<AccountNumber> totalList = new LinkedList<>();
		for(int i = 0; i < ssafyAccountDetailResponseList.getRec().size(); i++){
			totalList.add(new AccountNumber(ssafyAccountDetailResponseList.getRec().get(i).getAccountNo()));
		}

		// 서비스에 등록되지 않은 계좌만 반환
		List<AccountNumber> result = new ArrayList<>();
		while(!totalList.isEmpty()){
			String accountNum = totalList.poll().getAccountNum();
			if(!accountRepository.existsByAccountNum(accountNum)){
				result.add(new AccountNumber(accountNum));
			}
		}

		return result;
	}

}
