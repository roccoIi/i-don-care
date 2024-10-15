package com.idoncare.user.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.user.application.SmsService;
import com.idoncare.user.dto.req.LoginTelReq;
import com.idoncare.user.dto.req.LoginIntegrateSignUpReq;
import com.idoncare.user.dto.req.LoginIntegrateVerifyReq;
import com.idoncare.user.dto.req.LoginSimplePasswordReq;
import com.idoncare.user.dto.resp.LoginSimpleSignInResp;
import com.idoncare.user.dto.resp.UserRelationResp;
import com.idoncare.user.dto.resp.UserRelationViewResp;
import com.idoncare.user.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User API", description = "회원가입 및 정보확인을 위한 API")
public class UserController {

	@Autowired
	private SmsService smsService;

	@Operation(summary = "휴대전화 인증번호 발송", description = "로그인/회원가입 시 휴대전화 인증번호 발송")
	@PostMapping("/integrate/send")
	public ResponseEntity<?> sendVerificationCode(@RequestBody @Valid final LoginTelReq req) {
		smsService.processSend(req);
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@Operation(summary = "휴대전화 인증번호 검증", description = "휴대전화 인증번호 발송 후 검증")
	@PostMapping("/integrate/verify")
	public ResponseEntity<?> verifyCode(@RequestBody @Valid final LoginIntegrateVerifyReq req) {
		smsService.processVerify(req);
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@Operation(summary = "회원가입", description = "회원가입 진행")
	@PostMapping("/integrate/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid final LoginIntegrateSignUpReq req) {

		return ResponseEntity.ok(new CommonResponse<>());
	}

	@Operation(summary = "비밀번호 등록", description = "비밀번호 등록")
	@PostMapping("/simple")
	public ResponseEntity<?> registerPassword(@RequestBody @Valid final LoginSimplePasswordReq req) {
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@Operation(summary = "로그인", description = "비밀번호 확인")
	@PostMapping("/simple/signin")
	public ResponseEntity<?> signin(@RequestBody @Valid final LoginSimplePasswordReq req) {
		LoginSimpleSignInResp resp = new LoginSimpleSignInResp();
		return ResponseEntity.ok(CommonResponse.from(resp));
	}

	@Operation(summary = "유저 관계 조회", description = "유저의 관계 조회")
	@GetMapping("/relation")
	public ResponseEntity<?> getUserRelations() {
		UserRelationResp resp = new UserRelationResp();
		return ResponseEntity.ok(CommonResponse.from(resp));
	}

	@Operation(summary = "부모의 자녀 등록 요청", description = "부모가 자녀 등록 요청")
	@PostMapping("/relation/register")
	public ResponseEntity<?> registerChildRelation(@RequestBody @Valid final LoginTelReq req) {
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@Operation(summary = "자녀 정보 조회", description = "자녀 ID로 자녀 정보 조회")
	@GetMapping("/relation/child/")
	public ResponseEntity<?> getChildInfo(@RequestBody final Map<String, Integer> req) {
		UserRelationViewResp resp = new UserRelationViewResp();
		return ResponseEntity.ok(CommonResponse.from(resp));
	}
}
