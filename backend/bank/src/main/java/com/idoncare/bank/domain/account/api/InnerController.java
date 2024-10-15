package com.idoncare.bank.domain.account.api;

import java.util.List;

import com.idoncare.bank.domain.account.dto.req.requestClient.SendableRequest;
import com.idoncare.bank.domain.account.dto.resp.responseClient.BalanceInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.kafka.shaded.io.opentelemetry.proto.metrics.v1.Sum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.bank.domain.account.application.AccountService;
import com.idoncare.bank.domain.account.application.TransService;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountNumber;
import com.idoncare.bank.domain.account.dto.req.requestClient.CardTransHistory;
import com.idoncare.bank.domain.account.dto.resp.responseClient.AuthNumResponse;
import com.idoncare.bank.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/inner")
@Tag(name = "Inner API", description = "서비스 목업데이터 생성 및 데이터 확인을 위한 api 모음")
@CrossOrigin("*")
public class InnerController {

	private final TransService transService;
	private final AccountService accountService;

	@Operation(summary = "[완료] 카드 거래내역 생성", description = "카드거래 목업 제작을 위한 카드 거래내역 생성")
	@PostMapping("/cardTransHistory")
	public ResponseEntity<CommonResponse<?>> createCardTrans(@RequestBody final CardTransHistory cardTransHistory){
		log.info("========== 카드 거래내역 생성 ==========");
		transService.addCardTransHistory(cardTransHistory);
		log.info("========== 카드 거래내역 종료 ==========");
		return ResponseEntity.ok(CommonResponse.success());
	}

	@Operation(summary = "[완료] 1원 인증번호 확인", description = "1원인증 후 가장 최근의 인증번호 확인")
	@PostMapping("/accountHistory")
	public ResponseEntity<CommonResponse<?>> accountHistoryList(@RequestBody final AccountNumber accountNumber){
		log.info("========== 계좌 거래내역 조회 ==========");
		AuthNumResponse result = accountService.accountHistoryList(accountNumber);
		log.info("========== 계좌 거래내역 조회 종료 ==========");
		return ResponseEntity.ok(CommonResponse.from(result));
	}

	@Operation(summary = "[완료] 등록되지 않은 계좌", description = "현재 서비스에 등록되지 않은 ssafy 계좌 목록 반환")
	@GetMapping("/canRegister")
	public ResponseEntity<CommonResponse<?>> UnRegisterAccount(){
		log.info("========== 미등록 계좌 조회 ==========");
		List<AccountNumber> list = accountService.unRegisterAccount();
		log.info("========== 미등록 계좌 조회 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(list));
	}

	@Operation(summary = "[완료] 요청금액 확인", description = "현재 요청받은 금액을 송금할 수 있느지 요청자의 잔액 확인")
	@PostMapping("/check")
	public ResponseEntity<CommonResponse<?>> isSendable(@RequestBody final Long userId){
		log.info("========== 잔액 여부 확인 시작 ==========");
		BalanceInfo balance = accountService.getBalance(userId);
		log.info("========== 잔액 여부 확인 종료 ==========");
		return ResponseEntity.ok(CommonResponse.from(balance));

	}
}
