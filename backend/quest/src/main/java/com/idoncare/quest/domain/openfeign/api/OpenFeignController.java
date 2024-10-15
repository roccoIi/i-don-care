package com.idoncare.quest.domain.openfeign.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.quest.domain.openfeign.application.OpenFeignService;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Tag(name = "OpenFeign API", description = "OpenFeign api")
@Slf4j
@RestController
@RequestMapping("/inner")
public class OpenFeignController {

	private final OpenFeignService openFeignService;

	public OpenFeignController(OpenFeignService openFeignService) {
		this.openFeignService = openFeignService;
	}

	@Operation(summary = "유저 관계 추가 [완료]", description = "관계를 Table에 추가")
	@PostMapping("/relation")
	public ResponseEntity<CommonResponse<?>> addUser (@RequestBody @Valid RelationIdReq relationIdReq) {
		log.info("========== 유저 추가 시작 ==========");
		openFeignService.addUser(relationIdReq.getRelationId());
		log.info("========== 유저 추가 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}
}
