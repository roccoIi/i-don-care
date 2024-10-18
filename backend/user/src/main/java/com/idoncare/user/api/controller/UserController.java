package com.idoncare.user.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.user.application.auth.AuthManager;
import com.idoncare.user.application.auth.RefreshService;
import com.idoncare.user.application.user.SmsManager;
import com.idoncare.user.application.user.UserManager;
import com.idoncare.user.domain.user.User;
import com.idoncare.user.dto.req.IntegrateRoleReq;
import com.idoncare.user.dto.req.IntegrateSignUpReq;
import com.idoncare.user.dto.req.IntegrateVerifyReq;
import com.idoncare.user.dto.req.SimplePasswordRegistReq;
import com.idoncare.user.dto.req.SimplePasswordReq;
import com.idoncare.user.dto.req.TelReq;
import com.idoncare.user.dto.resp.LoginSimpleSignInResp;
import com.idoncare.user.dto.resp.UserSignUpResp;
import com.idoncare.user.global.common.exception.CommonResponse;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.exception.AuthorizedException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "User API", description = "회원가입 및 정보확인을 위한 API")
@Validated
public class UserController {

	private final SmsManager smsManager;
	private final UserManager userManager;
	private final AuthManager authManager;

	@PostMapping("/integrate/send")
	@Operation(summary = "[완료] 휴대전화 인증번호 발송", description = "로그인/회원가입 시 휴대전화 인증번호 발송")
	public ResponseEntity<?> sendVerificationCode(@RequestBody @Valid final TelReq req) {
		log.info("===휴대전화 인증번호 발송 START===");
		smsManager.processSend(req);
		log.info("===휴대전화 인증번호 발송 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@PostMapping("/integrate/verify")
	@Operation(summary = "[완료] 휴대전화 인증번호 검증", description = "휴대전화 인증번호 발송 후 검증")
	public ResponseEntity<?> verifyCode(@RequestBody @Valid final IntegrateVerifyReq req) {
		log.info("===휴대전화 인증번호 검증 START===");
		smsManager.processVerify(req);
		log.info("===휴대전화 인증번호 검증 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@PostMapping("/integrate/signup")
	@Operation(summary = "[완료] 통합 로그인 (회원가입)", description = "회원 아니면 회원가입 || 회원이면 최초 로그인")
	public ResponseEntity<?> signup(HttpServletResponse response,
		@RequestBody @Valid final IntegrateSignUpReq signUpReq) {
		log.info("===통합 회원가입/로그인 START===");

		boolean isExistUser = userManager.checkUserExist(new TelReq(signUpReq.getTel()));
		if (isExistUser) {
			log.info("===회원 존재 - 로그인 처리===");
			User user = userManager.findUser(signUpReq.getTel());
			authManager.createRefreshToken(response, signUpReq.getTel());
			UserSignUpResp signUpResp = new UserSignUpResp(user.getUserId(), isExistUser);
			log.info("===통합 회원가입/로그인 - 로그인 완료 END===");
			return ResponseEntity.ok().body(CommonResponse.from(signUpResp));
		} else {
			log.info("회원 존재하지 않음 - 회원가입 처리");
			UserSignUpResp signUpResp = userManager.signup(signUpReq);
			authManager.createRefreshToken(response, signUpReq.getTel());
			log.info("===통합 회원가입/로그인 - 회원가입 완료 END===");
			return ResponseEntity.ok(CommonResponse.from(signUpResp));
		}

	}

	@PostMapping("/integrate/signup/role")
	@Operation(summary = "[완료] 회원가입 후 역할 등록", description = "회원가입 후 역할 등록")
	public ResponseEntity<?> registerRole(@RequestBody @Valid final IntegrateRoleReq req) {
		log.info("===회원가입 후 역할 등록 START===");
		userManager.registerRole(req);
		log.info("===회원가입 후 역할 등록 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@DeleteMapping
	@Operation(summary = "[완료] 회원탈퇴", description = "회원탈퇴 진행")
	public ResponseEntity<?> signout(HttpServletRequest request,
		@RequestBody @Valid final SimplePasswordReq req) {
		log.info("===회원탈퇴 START===");
		String userIdHeader = request.getHeader("X-User-Id");
		if (userIdHeader == null) {
			throw new AuthorizedException(ErrorCode.A009);  // 인증 실패 처리
		}
		long userId = Long.parseLong(userIdHeader);
		userManager.signout(req, (Long)userId);
		log.info("===회원탈퇴 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@PostMapping("/simple")
	@Operation(summary = "[완료] 간편 로그인 - 비밀번호 등록", description = "비밀번호 등록")
	public ResponseEntity<?> registerPassword(@RequestBody @Valid final SimplePasswordRegistReq req) {
		log.info("===비밀번호 등록 START===");
		log.info("비밀번호 확인 {}", req);
		userManager.registerPassword(req);
		log.info("===비밀번호 등록 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@PatchMapping("/simple")
	@Operation(summary = "[완료] 간편 로그인 - 비밀번호 수정", description = "비밀번호 수정")
	public ResponseEntity<?> registerPassword(HttpServletRequest request,
		@RequestBody @Valid final SimplePasswordReq req) {
		log.info("===비밀번호 수정 START===");
		String userIdHeader = request.getHeader("X-User-Id");
		if (userIdHeader == null) {
			throw new AuthorizedException(ErrorCode.A009);  // 인증 실패 처리
		}
		long userId = Long.parseLong(userIdHeader);
		userManager.updatePassword(req, userId);
		log.info("===비밀번호 수정 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@PostMapping("/simple/signin")
	@Operation(summary = "[완료] 간편 로그인", description = "비밀번호 확인")
	public ResponseEntity<?> signin(HttpServletRequest request,
		@RequestBody @Valid final SimplePasswordReq req) {
		log.info("===간편 로그인 START===");
		log.info("로그인 정보 확인 {}", req);
		Long userId = authManager.getUserIdFromRefreshToken(request);
		System.out.println("Refresh Token 으로부터 얻은 USER ID : " + userId);
		LoginSimpleSignInResp resp = userManager.signin(req, userId);
		String accessToken = authManager.refreshAccessToken(request);
		log.info("===간편 로그인 END===");
		return ResponseEntity
			.ok()
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.body(CommonResponse.from(resp));
	}

	@PostMapping("/reissue")
	@Operation(summary = "[완료] 간편 로그인", description = "비밀번호 확인")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
		log.info("===엑세스토큰 재발급 START===");
		String accessToken = authManager.refreshAccessToken(request);
		log.info("===엑세스토큰 재발급 END===");
		return ResponseEntity.noContent()
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.build();
	}

	@PostMapping("/integrate/logout")
	@Operation(summary = "[완료] 로그아웃", description = "로그아웃 진행")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		log.info("===로그아웃 START===");
		authManager.deleteRefreshToken(request, response);
		log.info("===로그아웃 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@PostMapping("/integrate/signup/parent-test")
	@Operation(summary = "부모 테스트 계정으로 로그인", description = "부모 테스트 계정으로 로그인")
	public ResponseEntity<?> testSigninByParent(HttpServletResponse response) {
		log.info("===부모 테스트 계정으로 로그인 START===");
		User user = userManager.findUser("01099999999");
		authManager.createRefreshToken(response, user.getUserPhone());
		UserSignUpResp signUpResp = new UserSignUpResp(user.getUserId(), true);
		log.info("===부모 테스트 계정으로 로그인 START===");
		return ResponseEntity.ok().body(CommonResponse.from(signUpResp));
	}

	@PostMapping("/integrate/signup/child-test")
	@Operation(summary = "자녀 테스트 계정으로 로그인", description = "자녀 테스트 계정으로 로그인")
	public ResponseEntity<?> testSigninByChild(HttpServletResponse response) {
		log.info("===자녀 테스트 계정으로 로그인 START===");
		User user = userManager.findUser("01088888888");
		authManager.createRefreshToken(response, user.getUserPhone());
		UserSignUpResp signUpResp = new UserSignUpResp(user.getUserId(), true);
		log.info("===자녀 테스트 계정으로 로그인 START===");
		return ResponseEntity.ok().body(CommonResponse.from(signUpResp));
	}
}
