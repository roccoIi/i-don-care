package com.idoncare.bank.domain.account.application;

import static com.idoncare.bank.global.common.ErrorCode.*;

import java.util.HashMap;

import com.idoncare.bank.domain.account.dto.kafka.LessBalance;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.bank.domain.account.application.kafka.KafkaProducer;
import com.idoncare.bank.domain.account.application.openFeign.AccountFeign;
import com.idoncare.bank.domain.account.application.openFeign.TransactionFeign;
import com.idoncare.bank.domain.account.application.openFeign.UserFeign;
import com.idoncare.bank.domain.account.dto.kafka.CompleteTransaction;
import com.idoncare.bank.domain.account.dto.req.requestClient.SendInfo;
import com.idoncare.bank.domain.account.dto.resp.responseServer.AccountTransfer;
import com.idoncare.bank.domain.account.dto.resp.responseServer.SsafyAccountDetailResponse;
import com.idoncare.bank.domain.account.dto.resp.responseServer.TransactionHistory;
import com.idoncare.bank.domain.account.entity.Account;
import com.idoncare.bank.domain.account.entity.Trans;
import com.idoncare.bank.domain.account.repository.AccountRepository;
import com.idoncare.bank.domain.account.repository.TransRepository;
import com.idoncare.bank.domain.account.repository.openFeign.BankOpenFeign;
import com.idoncare.bank.global.exception.JsonParsingException;
import com.idoncare.bank.global.exception.NotFoundException;
import com.idoncare.bank.global.util.HeaderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaListenService {

	private final HeaderUtil headerUtil;
	private final BankOpenFeign bankOpenFeign;
	private final AccountRepository accountRepository;
	private final TransRepository transRepository;
	private final KafkaProducer kafkaProducer;
	private final AccountFeign accountFeign;
	private final TransactionFeign transactionFeign;
	private final ObjectMapper objectMapper;
	private final UserFeign userFeign;

	/**
	 * 거래 진행 양측 계좌정보의 잔액을 업데이트 한다.
	 * @param message
	 */
	@Transactional
	@KafkaListener(topics = "complete-transaction", groupId = "account")
	public void updateAccount(String message){
		log.info("account 객체 업데이트: -> {}", message);

		// kafka 객체 받아서 내용 분리
		HashMap<Object, Object> map = parseMessage(message);

		String accountNumber = map.get("accountNumber").toString();

		// 회원이 아닌 계좌번호라면 종료
		if(!accountRepository.existsByAccountNum(accountNumber)) return;

		// (1) SSAFY DB에서 테이터를 불러오고,
		SsafyAccountDetailResponse accountDetail = accountFeign.getOneAccountDetail(accountNumber);

		// (2) idoncare DB에서 해당 계좌번호를 가진 객체를 찾아서
		Account account = accountRepository.findByAccountNum(accountNumber)
			.orElseThrow(() -> new NotFoundException(B000));

		// (3) SSAFY DB 객체의 balance를 idoncare DB 객체에 업데이트 후 저장
		account.setBalance(accountDetail.getRec().getAccountBalance());
		accountRepository.save(account);

	}

	/**
	 * (Kafka) 거래 이후 해당 거래내역을 idoncare DB로 이관한다.
	 * @param message
	 */
	@Transactional
	@KafkaListener(topics = "complete-transaction", groupId = "trans")
	public void copyTransactionHistory(String message){
		log.info("거래내역 복사 시작 -> {}", message);

		// kafka 객체 받아서 내용 분리
		HashMap<Object, Object> map = parseMessage(message);

		String accountNumber = map.get("accountNumber").toString();
		Long uniqueNumber = Long.valueOf(map.get("uniqueNumber").toString());

		if(!accountRepository.existsByAccountNum(accountNumber)) return;

		// SSAFY DB에서 해당 거래번호의 거래내역 불러온다.
		TransactionHistory transactionHistory = transactionFeign.getTransactionHistoryDetail(accountNumber, uniqueNumber);

		// 해당 계좌번호의 계좌 객체 불러와 (비회원일경우 accountId = null)
		Trans trans;
		if(accountRepository.existsByAccountNum(accountNumber)){
			Account account = accountRepository.findByAccountNum(accountNumber)
				.orElseThrow(() -> new NotFoundException(B000));

			// 이제 우리 DB에 집어넣어
			trans = Trans.toEntity(account, transactionHistory.getRec());
		} else {
			trans = Trans.toEntity(transactionHistory.getRec());
		}

		// 이제 우리 DB에 집어넣어
		transRepository.save(trans);
	}

	/**
	 * 본인확인 없이 송금을 진행한다. (자동이체 전용)
	 * @param message
	 */
	@Transactional
	@KafkaListener(
		topics =  "savings-progress-user",
		groupId = "trans"
	)
	public void giveMoneyToAuto(String message) {
		log.info("자동이체 시작 -> {}", message);

		// 송금 방향 결정 (complete가 포함된 토픽이면 부모 -> 자식, process가 포함된 토픽이면 자식 -> 부모)

		// kafka 객체 받아서 내용 분리
		HashMap<Object, Object> map = parseMessage(message);

			String sendAccountNum = accountRepository.findAccountNumByUserId(Long.valueOf(map.get("childId").toString()))
					.orElseThrow(() -> new NotFoundException(B004));
		String receiveAccountNum = accountRepository.findAccountNumByUserId(Long.valueOf(map.get("parentId").toString()))
					.orElseThrow(() -> new NotFoundException(B004));


		Long amount = Long.valueOf(map.get("amount").toString());
		Long interestAmount = 0L;

		Long nowAmount = accountRepository.findBalanceByAccountNum(sendAccountNum)
				.orElseThrow(() -> new NotFoundException(B004));
		if(nowAmount < amount) {
			Long sendUserId = accountRepository.findUserIdByAccountNum(sendAccountNum)
					.orElseThrow(() -> new NotFoundException(B004));
			kafkaProducer.send("less-balance", LessBalance.toEntity(sendUserId, amount, nowAmount, amount - nowAmount));
			log.info("================ 잔액 부족 kafka 발송 완료 ===================");
			throw new NotFoundException(B005);
		}


		if(map.containsKey("interestAmount")){
			interestAmount = Long.valueOf(map.get("interestAmount").toString());
		}
		SendInfo sendInfo = new SendInfo(sendAccountNum, receiveAccountNum, amount + interestAmount);

		// Openfeign
		AccountTransfer accountTransfer = transactionFeign.sendMoney(sendInfo);

		// 양쪽 게좌 거래내역에 각각 추가
		accountTransfer.getRec().forEach(record ->
			kafkaProducer.send("complete-transaction",
				CompleteTransaction.toEntity(record.getAccountNo(), record.getTransactionUniqueNo())));

	}

	@Transactional
	@KafkaListener(
		topics = {"savings-complete-user", "mission-complete-user", "quiz-complete-user"},
		groupId = "trans"
	)
	public void giveMoneyToMOM(String message) {
		log.info("자동이체 시작 -> {}", message);

		// 송금 방향 결정 (complete가 포함된 토픽이면 부모 -> 자식, process가 포함된 토픽이면 자식 -> 부모)

		// kafka 객체 받아서 내용 분리
		HashMap<Object, Object> map = parseMessage(message);


			String sendAccountNum = accountRepository.findAccountNumByUserId(Long.valueOf(map.get("parentId").toString()))
				.orElseThrow(() -> new NotFoundException(B004));
			String receiveAccountNum = accountRepository.findAccountNumByUserId(Long.valueOf(map.get("childId").toString()))
				.orElseThrow(() -> new NotFoundException(B004));


		Long amount = Long.valueOf(map.get("amount").toString());
		Long interestAmount = 0L;

		Long nowAmount = accountRepository.findBalanceByAccountNum(sendAccountNum)
			.orElseThrow(() -> new NotFoundException(B004));
		if(nowAmount < amount) {
			Long sendUserId = accountRepository.findUserIdByAccountNum(sendAccountNum)
				.orElseThrow(() -> new NotFoundException(B004));
			kafkaProducer.send("less-balance", LessBalance.toEntity(sendUserId, amount, nowAmount, amount - nowAmount));
			log.info("================ 잔액 부족 kafka 발송 완료 ===================");
			throw new NotFoundException(B005);
		}


		if(map.containsKey("interestAmount")){
			interestAmount = Long.valueOf(map.get("interestAmount").toString());
		}
		SendInfo sendInfo = new SendInfo(sendAccountNum, receiveAccountNum, amount + interestAmount);

		// Openfeign
		AccountTransfer accountTransfer = transactionFeign.sendMoney(sendInfo);

		// 양쪽 게좌 거래내역에 각각 추가
		accountTransfer.getRec().forEach(record ->
			kafkaProducer.send("complete-transaction",
				CompleteTransaction.toEntity(record.getAccountNo(), record.getTransactionUniqueNo())));

	}

	/**
	 * String으로 들어온 Kafka 내용을 Map으로 변환하여 매핑
	 * @param message
	 * @return
	 */
	private HashMap<Object, Object> parseMessage(String message) {
		try {
			return objectMapper.readValue(message, new TypeReference<>() {});
		} catch (JsonProcessingException e) {
			throw new JsonParsingException(G003);
		}
	}
}
