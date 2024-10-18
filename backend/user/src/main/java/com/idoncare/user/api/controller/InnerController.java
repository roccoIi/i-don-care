package com.idoncare.user.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.user.application.user.UserManager;
import com.idoncare.user.dto.UserInfoDto;
import com.idoncare.user.global.common.exception.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/inner")
@RequiredArgsConstructor
@Tag(name = "내부 통신용 API", description = "내부 통신을 위한 API")
@Validated
public class InnerController {

	private final UserManager userManager;

	@PostMapping("/names")
	@Operation(summary = "[완료] 유저 ID -> Name 반환", description = "내부 통신용 유저 ID -> Name 반환")
	public ResponseEntity<?> getUserNames(@RequestBody final List<Long> userIds) {
		log.info("===유저 ID -> Name 반환 START===");
		List<UserInfoDto> userNames = userManager.getUserNameFromIds(userIds);
		log.info("===유저 ID -> Name 반환 END===");
		return ResponseEntity.ok().body(CommonResponse.from(userNames));
	}
}
