package com.idoncare.user.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.user.application.relation.RelationManager;
import com.idoncare.user.dto.req.RelationDeleteReq;
import com.idoncare.user.dto.req.RelationResponseReq;
import com.idoncare.user.dto.req.TelReq;
import com.idoncare.user.dto.resp.UserRelationResp;
import com.idoncare.user.global.common.exception.CommonResponse;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.exception.AuthorizedException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/relation")
@RequiredArgsConstructor
@Tag(name = "RELATION API", description = "부모/자녀 관계를 위한 API")
@Validated
public class RelationController {

	@Autowired
	private RelationManager relationManager;

	@GetMapping
	@Operation(summary = "[완료] 유저 관계 조회", description = "유저의 관계 조회")
	public ResponseEntity<?> getUserRelations(HttpServletRequest request) {
		log.info("===유저 관계 조회 START===");
		String userIdHeader = request.getHeader("X-User-Id");
		if (userIdHeader == null) {
			throw new AuthorizedException(ErrorCode.A009);  // 인증 실패 처리
		}
		long userId = Long.parseLong(userIdHeader);
		UserRelationResp resp = relationManager.getUserRelations(userId);
		log.info("===유저 관계 조회 END===");
		return ResponseEntity.ok(CommonResponse.from(resp));
	}

	@PostMapping("/register")
	@Operation(summary = "[완료] 부모의 자녀 등록 요청 (부모만 해당)", description = "부모가 자녀 등록 요청 (부모만 해당)")
	public ResponseEntity<?> registerChildRelation(HttpServletRequest request,
		@RequestBody @Valid final TelReq req) {
		log.info("===부모의 자녀 등록 요청 START===");
		String userIdHeader = request.getHeader("X-User-Id");
		if (userIdHeader == null) {
			throw new AuthorizedException(ErrorCode.A009);  // 인증 실패 처리
		}
		long userId = Long.parseLong(userIdHeader);
		relationManager.processRelationRequest(req, userId);
		log.info("===부모의 자녀 등록 요청 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@PostMapping("/response")
	@Operation(summary = "[완료] 관계 요청 응답 (자녀만 해당)", description = "자녀가 등록요청에 대해 응답 (자녀만 해당)")
	public ResponseEntity<?> responseRelation(@RequestBody @Valid final RelationResponseReq req) {
		log.info("===관계 요청 응답 START===");
		relationManager.processRelationResponse(req);
		log.info("===관계 요청 응답 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@DeleteMapping
	@Operation(summary = "[완료] 관계 삭제", description = "관계 삭제")
	public ResponseEntity<?> registerChildRelation(@RequestBody @Valid final RelationDeleteReq req) {
		log.info("===관계 삭제 START===");
		relationManager.deleteRelation(req);
		log.info("===관계 삭제 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

}
