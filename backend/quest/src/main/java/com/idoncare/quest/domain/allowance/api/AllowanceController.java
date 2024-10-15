package com.idoncare.quest.domain.allowance.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.quest.domain.allowance.application.service.AllowanceService;
import com.idoncare.quest.domain.allowance.dto.req.AllowanceIdReq;
import com.idoncare.quest.domain.allowance.dto.req.AllowanceReq;
import com.idoncare.quest.domain.allowance.dto.resp.AllowanceDetailResp;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Tag(name = "Allowance API", description = "용돈과 관련된 api")
@Slf4j
@RestController
@RequestMapping("/allowance")
public class AllowanceController {

	private final AllowanceService allowanceService;

	public AllowanceController(AllowanceService allowanceService) {
		this.allowanceService = allowanceService;
	}

	@Operation(summary = "용돈 조회 [완료]", description = "접속한 유저의 용돈 주기, 일자, 금액을 조회")
	@PostMapping("/detail")
	public ResponseEntity<CommonResponse<?>> detailAllowance(@RequestBody @Valid RelationIdReq relationIdReq) {
		log.info("========== 용돈 설정 시작 ==========");
		AllowanceDetailResp allowanceDetailResp = allowanceService.detailAllowance(relationIdReq);
		log.info("========== 용돈 설정 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(allowanceDetailResp));
	}

	@Operation(summary = "용돈 등록 [완료]", description = "접속한 유저의 용돈 주기, 일자, 금액을 등록")
	@PostMapping("/regist")
	public ResponseEntity<CommonResponse<?>> registAllowance(@RequestBody @Valid AllowanceReq allowanceReq) {
		log.info("========== 용돈 설정 시작 ==========");
		allowanceService.registAllowance(allowanceReq);
		log.info("========== 용돈 설정 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}

	@Operation(summary = "용돈 설정 변경 [완료]", description = "접속한 유저의 용돈 주기, 일자, 금액을 변경")
	@PostMapping("/modify")
	public ResponseEntity<CommonResponse<?>> modifyAllowance(@RequestBody @Valid AllowanceReq allowanceReq) {
		log.info("========== 용돈 변경 시작 ==========");
		allowanceService.modifyAllowance(allowanceReq);
		log.info("========== 용돈 변경 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}

	@Operation(summary = "용돈 삭제 변경 [완료]", description = "접속한 유저의 용돈 주기, 일자, 금액을 삭제")
	@PutMapping("/delete")
	public ResponseEntity<CommonResponse<?>> deleteAllowance(@RequestBody @Valid AllowanceIdReq allowanceIdReq) {
		log.info("========== 용돈 삭제 시작 ==========");
		allowanceService.deleteAllowance(allowanceIdReq);
		log.info("========== 용돈 삭제 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}
}
