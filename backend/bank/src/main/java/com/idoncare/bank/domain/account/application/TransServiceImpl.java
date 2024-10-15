package com.idoncare.bank.domain.account.application;

import static com.idoncare.bank.global.common.ErrorCode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.idoncare.bank.domain.account.application.openFeign.AccountFeign;
import com.idoncare.bank.domain.account.application.openFeign.UserFeign;
import com.idoncare.bank.domain.account.dto.kafka.LessBalance;
import com.idoncare.bank.domain.account.dto.req.requestClient.CardTransHistory;
import com.idoncare.bank.domain.account.dto.resp.responseClient.UserInfoDto;
import com.idoncare.bank.domain.account.dto.resp.responseServer.CardDetailList;
import com.idoncare.bank.domain.account.dto.resp.responseServer.CardTransResult;
import com.idoncare.bank.domain.account.entity.CardTrans;
import com.idoncare.bank.domain.account.repository.CardTransRepository;
import com.idoncare.bank.global.exception.JsonParsingException;
import com.idoncare.bank.global.exception.UnAuthorizedException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.bank.domain.account.application.kafka.KafkaProducer;
import com.idoncare.bank.domain.account.application.openFeign.TransactionFeign;
import com.idoncare.bank.domain.account.dto.kafka.CompleteTransaction;
import com.idoncare.bank.domain.account.dto.req.requestClient.SendInfo;
import com.idoncare.bank.domain.account.dto.resp.responseClient.RecentTransactionPeople;
import com.idoncare.bank.domain.account.dto.resp.responseClient.TransactionHistoryInfo;
import com.idoncare.bank.domain.account.dto.resp.responseServer.AccountTransfer;
import com.idoncare.bank.domain.account.dto.resp.responseServer.TransactionHistory;
import com.idoncare.bank.domain.account.entity.Account;
import com.idoncare.bank.domain.account.entity.Trans;
import com.idoncare.bank.domain.account.repository.AccountRepository;
import com.idoncare.bank.domain.account.repository.TransRepository;
import com.idoncare.bank.global.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransServiceImpl implements TransService {

	private final AccountRepository accountRepository;
	private final TransRepository transRepository;
	private final TransactionFeign transactionFeign;
	private final ObjectMapper objectMapper;
	private final KafkaProducer kafkaProducer;
	private final AccountFeign accountFeign;
	private final UserFeign userFeign;
	private final CardTransRepository cardTransRepository;

	/**
	 * userId를 통해 접속한 유저가 계좌의 주인이 맞는지 확인 후 idoncare DB 내 계좌 거래내역을 반환한다.
	 * @param userId
	 * @param accountNum
	 * @return
	 */
	@Override
	public TransactionHistoryInfo getTransactionList(Long userId, String accountNum) {
		// 게좌 소유주 확인
		Long findUserId = accountRepository.findUserIdByAccountNum(accountNum)
		 	.orElseThrow(() -> new NotFoundException(B004));

		 if(!Objects.equals(findUserId, userId)){
		 	throw new UnAuthorizedException(B002);
		 }

		 // 거래내역 불러오기
		List<Trans> list = transRepository.findByAccountNumDESC(accountNum);
		int count = list.size();

		List<TransactionHistoryInfo.MiniTransactionHistoryInfo> miniTransactionHistoryInfo = list.stream()
			.map(Trans::toTransactionInfo)
			.toList();

		return TransactionHistoryInfo.builder()
			.count(count)
			.list(miniTransactionHistoryInfo)
			.build();
	}


	/**
	 * userId를 통해 접속한 유저가 계좌의 주인이 맞는지 확인 후 송금을 진행한다.
	 * @param userId
	 * @param sendInfo
	 */
	@Override
	@Transactional
	public void giveMoneyToComm(Long userId, SendInfo sendInfo) {
		// 계좌 소유주 확인
		 String sendAccountNum = accountRepository.findAccountNumByUserId(userId)
			 .orElseThrow(() -> new NotFoundException(B004));

	    // 잔액 확인 후 만일 잔액 부족하다면 오류 반환
		Long balance = accountRepository.findBalanceByAccountNum(sendAccountNum)
			.orElseThrow(() -> new NotFoundException(B004));

	    if(balance < sendInfo.getAmount()) {
			Account account = accountRepository.findByAccountNum(sendAccountNum)
					.orElseThrow(() -> new NotFoundException(B004));
			kafkaProducer.send("less-balance", LessBalance.toEntity(account, sendInfo.getAmount(), sendInfo.getAmount() - account.getBalance()));
			log.info("================ 잔액 부족 kafka 발송 완료 ===================");
			throw new NotFoundException(B005);
		}

		// Openfeign
		AccountTransfer accountTransfer = transactionFeign.sendMoney(sendInfo);

		// 양쪽 게좌 거래내역에 각각 추가
		for(int i = 0; i < 2; i++){
			String accountNumber = accountTransfer.getRec().get(i).getAccountNo();
			Long uniqueNumber = accountTransfer.getRec().get(i).getTransactionUniqueNo();
			kafkaProducer.send("complete-transaction", CompleteTransaction.toEntity(accountNumber, uniqueNumber));
		}
	}


	/**
	 * 최근 거래한 계좌의 게좌번호와 은행을 반환한다.
	 * @param userId
	 * @return
	 */
	@Override
	public List<RecentTransactionPeople> getTransactionNameList(Long userId) {
		// 계좌번호, 은행명 입력
		List<String> list =  transRepository.findAccountNumByAccountId(userId); //accountId 모음집
		List<RecentTransactionPeople> result = new ArrayList<>();
		for (String s : list) {
			result.add(RecentTransactionPeople.of(accountFeign.getOneAccountDetail(s)));
		}

		// UserId를 이용한 유저정보 입력
		List<Long> userIds = new ArrayList<>();
		Long[] ids = new Long[list.size()];
		for(int i = 0; i < list.size(); i++) {
			Long id = accountRepository.findUserIdByAccountNumNull(list.get(i));
			if(id != null){
				userIds.add(id);
			}
			ids[i] = id;
		}

		UserInfoDto userDetail = userFeign.getUserInfoDtoByUserId(userIds);
		int num = 0;
		for(int i = 0; i < userIds.size(); i++) {
			if(ids[i] != null) {
				String username = userDetail.getData().get(num++).getUserName();
				result.get(i).setUserName(username);
			}
		}

		return result;
	}

	/**
	 * 카드 거래내역 생성
	 * @param cardTransHistory
	 * @return
	 */
	@Override
	public CardTransResult addCardTransHistory(CardTransHistory cardTransHistory) {
		CardTransResult result =  transactionFeign.makeCardTransHistory(cardTransHistory);

		// 거래내역 우리 DB로 이관
		String accountNum = null;
		CardDetailList detailList = transactionFeign.getCreditCardList();
		for(int i = 0; i < detailList.getRec().size(); i++){
			if(cardTransHistory.getCardNo().equals(detailList.getRec().get(i).getCardNo())){
				accountNum = detailList.getRec().get(i).getWithdrawalAccountNo();
			}
		}

		Account account = accountRepository.findByAccountNum(accountNum)
			.orElseThrow(() -> new NotFoundException(B004));

		cardTransRepository.save(CardTrans.toEntity(result.getResult(), account));

		return result;
	}

}
