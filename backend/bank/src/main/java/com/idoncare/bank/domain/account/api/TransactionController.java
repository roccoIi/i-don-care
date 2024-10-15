package com.idoncare.bank.domain.account.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.idoncare.bank.domain.account.application.KafkaListenService;
import com.idoncare.bank.domain.account.dto.req.requestClient.CardTransHistory;
import com.idoncare.bank.global.exception.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.bank.domain.account.application.TransService;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountNumber;
import com.idoncare.bank.domain.account.dto.req.requestClient.SendInfo;
import com.idoncare.bank.domain.account.dto.resp.RecentUser_temp;
import com.idoncare.bank.domain.account.dto.resp.responseClient.RecentTransactionPeople;
import com.idoncare.bank.domain.account.dto.resp.responseClient.TransactionHistoryInfo;
import com.idoncare.bank.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.idoncare.bank.global.common.ErrorCode.A009;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Tag(name = "Transaction API", description = "거래와 관련 api")
@CrossOrigin("*")
public class TransactionController {

	private final TransService transService;
	private final KafkaListenService kafkaListenService;

	@Operation(summary = "[완료] 거래내역 조회",  description = "해당 계좌 내 거래내역을 조회한다.(입출금 및 결제내역 포함)")
	@PostMapping("/statement")
	public ResponseEntity<CommonResponse<?>> checkBankStatement(@RequestBody final AccountNumber accountNumber, HttpServletRequest request) {
		log.info("========== 거래내역 조회 시작 ==========");
		Long userId = Optional.ofNullable(request.getHeader("X-User-Id"))
				.map(Long::parseLong)
				.orElseThrow(() -> new UnAuthorizedException(A009));
		TransactionHistoryInfo list = transService.getTransactionList(userId, accountNumber.getAccountNum());
		log.info("========== 거래내역 조회 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(list));
	}

	@Operation(summary = "[완료] 계좌이체_일반",  description = "지정한 인물 또는 특정 계좌에 계좌이체 진행")
	@PutMapping("/send/comm")
	public ResponseEntity<CommonResponse<?>> SendMoneyToComm(@RequestBody final SendInfo sendInfo, HttpServletRequest request) {
		log.info("========== 계좌 이체 시작(일반) ==========");
		Long userId = Optional.ofNullable(request.getHeader("X-User-Id"))
				.map(Long::parseLong)
				.orElseThrow(() -> new UnAuthorizedException(A009));
		transService.giveMoneyToComm(userId, sendInfo);
		log.info("========== 계좌 이체 종료(일반) ==========");
		return ResponseEntity.ok(CommonResponse.success());
	}

	@Operation(summary = "[완료] 계좌이체_자동",  description = "Kafka 로 자동이체만 진행할 예정")
	@PutMapping("/send/auto")
	public ResponseEntity<CommonResponse<?>> SendMoneyToAuto(@RequestBody final String messgae) {
		log.info("========== 계좌 이체 시작(자동) ==========");
		kafkaListenService.giveMoneyToAuto(messgae);
		log.info("========== 계좌 이체 종료(자동) ==========");
		return ResponseEntity.ok(CommonResponse.success());
	}

	@Operation(summary = "[완료] 최근 송금인원 반환", description = "접속한 유저가 최근 송금한 계좌번호 반환 (최대 10개)")
	@GetMapping("/send/recent")
	public ResponseEntity<CommonResponse<?>> recentSendList(HttpServletRequest request) {
		log.info("========== 최근 송금내역 리스트반환 시작 ==========");
		Long userId = Optional.ofNullable(request.getHeader("X-User-Id"))
				.map(Long::parseLong)
				.orElseThrow(() -> new UnAuthorizedException(A009));
		List<RecentTransactionPeople> list = transService.getTransactionNameList(userId);
		log.info("========== 최근 송금내역 리스트반환 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(list));
	}

}
