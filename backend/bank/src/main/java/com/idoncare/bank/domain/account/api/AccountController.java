package com.idoncare.bank.domain.account.api;

import com.idoncare.bank.global.exception.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.bank.domain.account.application.AccountService;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountAuth;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountNumber;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountDetail;
import com.idoncare.bank.domain.account.dto.resp.responseClient.UserAccountDetail;
import com.idoncare.bank.domain.account.dto.resp.responseClient.BalanceInfo;
import com.idoncare.bank.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.idoncare.bank.global.common.ErrorCode.A009;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Tag(name = "Account API", description = "등록고객 계좌와 관련된 api")
@CrossOrigin("*")
public class AccountController {

	private final AccountService accountService;

	@Operation(summary = "[완료] 1원 송금", description = "계좌 본인 소유 확인을 위해 1원 송금")
	@PostMapping("/auth")
	public ResponseEntity<CommonResponse<?>> sendAccountAuthNum(@RequestBody final AccountNumber accountNumber) {
		log.info("========== 1원 인증 발송 시작 ==========");
		accountService.sendAuthNum(accountNumber.getAccountNum());
		log.info("========== 1원 인증 발송 완료 ==========");
		return ResponseEntity.ok(CommonResponse.success());
	}

	@Operation(summary = "[완료] 1원 송금 검증", description = "1원과 함께 발송된 인증번호 입력 시 계좌등록 완료")
	@PostMapping("/auth/check")
	public ResponseEntity<CommonResponse<?>> checkAccountAuthNum(@RequestBody final AccountAuth accountAuth, HttpServletRequest request) {
		log.info("========== 1원 인증 시작 ==========");
		Long userId = Optional.ofNullable(request.getHeader("X-User-Id"))
				.map(Long::parseLong)
				.orElseThrow(() -> new UnAuthorizedException(A009));
		accountService.checkAuthNum(accountAuth);
		log.info("========== 1원 인증 완료 ==========");
		log.info("========== 계좌 및 거래내역 등록 시작 ==========");
		accountService.registAccount(accountAuth, userId);
		log.info("========== 계좌 및 거래내역 등록 완료 ==========");
		return ResponseEntity.ok(CommonResponse.success());
	}

	@Operation(summary = "[완료] 잔액 조회",  description = "해당 계좌에 남아있는 잔액 조회")
	@GetMapping("/remain")
	public ResponseEntity<CommonResponse<?>> checkBalance(HttpServletRequest request) {
		log.info("========== 잔액 조회 시작 ==========");
		Long userId = Optional.ofNullable(request.getHeader("X-User-Id"))
				.map(Long::parseLong)
				.orElseThrow(() -> new UnAuthorizedException(A009));
		BalanceInfo balance = accountService.getBalance(userId);
		log.info("========== 잔액 조회 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(balance));
	}

	@Operation(summary = "[완료] 사용자 계좌 조회", description = "접속한 유저가 등록한 계좌의 상세내용을 반환한다.")
	@GetMapping("/detail")
	public ResponseEntity<CommonResponse<?>> accountDetail(HttpServletRequest request) {
		log.info("========== 계좌 조회 시작 ==========");
		Long userId = Optional.ofNullable(request.getHeader("X-User-Id"))
				.map(Long::parseLong)
				.orElseThrow(() -> new UnAuthorizedException(A009));

		UserAccountDetail detail = accountService.getAccountDetail(userId);
		log.info("========== 계좌 조회 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(detail));
	}

	@Operation(summary = "[완료] 예금주 조회", description = "특정 계좌의 에금주를 조회한다.")
	@PostMapping("/holder")
	public ResponseEntity<CommonResponse<?>> accountOner(@RequestBody final AccountNumber accountNumber) {
		log.info("========== 예금주 조회 시작 ==========");
		String name = accountService.getUserId(accountNumber);
		log.info("========== 예금주 조회 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(name));
	}

	@Operation(summary = "[완료] 등록 후 카드 연동", description = "계좌가 이미 등록된 사용자가 사후 카드 연동을 원할경우")
	@PostMapping("/regist/card")
	public ResponseEntity<CommonResponse<?>> registCard(HttpServletRequest request){
		log.info("========== 카드 연동 시작 ==========");
		Long userId = Optional.ofNullable(request.getHeader("X-User-Id"))
			.map(Long::parseLong)
			.orElseThrow(() -> new UnAuthorizedException(A009));
		accountService.registCard(userId);
		log.info("========== 카드 연동 완료 ==========");
		return ResponseEntity.ok(CommonResponse.success());
	}



}

