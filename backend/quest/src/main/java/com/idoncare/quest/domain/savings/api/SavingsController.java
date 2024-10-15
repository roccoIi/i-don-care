package com.idoncare.quest.domain.savings.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.domain.savings.application.service.SavingsService;
import com.idoncare.quest.domain.savings.dto.req.AddAmountReq;
import com.idoncare.quest.domain.savings.dto.req.CancelCoinboxReq;
import com.idoncare.quest.domain.savings.dto.req.CoinboxReq;
import com.idoncare.quest.domain.savings.dto.resp.ChildCoinboxDetailResp;
import com.idoncare.quest.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Tag(name = "Savings API", description = "저금통과 관련된 api")
@Slf4j
@RestController
@RequestMapping("/savings")
public class SavingsController {

	private final SavingsService savingsService;

	public SavingsController(SavingsService savingsService) {
		this.savingsService = savingsService;
	}

	@Operation(summary = "저금통 목표 생성 [완료]", description = "저금통의 목표를 생성")
	@PostMapping("/regist")
	public ResponseEntity<CommonResponse<?>> registCoinbox(@RequestBody @Valid final CoinboxReq coinboxReq) {
		log.info("========== 목표 생성 시작 ==========");
		savingsService.registCoinbox(coinboxReq);
		log.info("========== 목표 생성 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}

	@Operation(summary = "저금통 목표 취소 [완료]", description = "저금통의 목표를 취소")
	@PutMapping("/cancel")
	public ResponseEntity<CommonResponse<?>> cancelCoinbox(@RequestBody @Valid final CancelCoinboxReq cancelCoinboxReq) {
		log.info("========== 목표 취소 시작 ==========");
		savingsService.cancelCoinbox(cancelCoinboxReq);
		log.info("========== 목표 취소 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}

	@Operation(summary = "저금통 상황 조회 [완료]", description = "저금통 상황을 조회")
	@PostMapping("/childDetail")
	public ResponseEntity<CommonResponse<?>> getChildDetail(@RequestBody @Valid final RelationIdReq relationIdReq) {
		log.info("========== 저금통 상황 조회 시작 ==========");
		ChildCoinboxDetailResp childCoinboxDetailResp = savingsService.getChildDetail(relationIdReq);
		log.info("========== 저금통 상황 조회 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(childCoinboxDetailResp));
	}

	@Operation(summary = "저금통 저금 진행 [완료]", description = "자신의 저금통에 저금 진행")
	@PutMapping("/addA")
	public ResponseEntity<CommonResponse<?>> addAmount(@RequestBody @Valid final AddAmountReq addAmountReq, HttpServletRequest request) {
		log.info("========== 저금통 저금 진행 시작 ==========");
		savingsService.addAmount(addAmountReq, request);
		log.info("========== 저금통 저금 진행 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}

	@Operation(summary = "저금통 이자 등록 [완료]", description = "부모님이 자식의 저금통에 이자 등록")
	@PutMapping("/addI")
	public ResponseEntity<CommonResponse<?>> addInterest(@RequestBody @Valid final AddAmountReq addAmountReq) {
		log.info("========== 저금통 이자 등록 시작 ==========");
		savingsService.addInterest(addAmountReq);
		log.info("========== 저금통 이자 등록 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}
}