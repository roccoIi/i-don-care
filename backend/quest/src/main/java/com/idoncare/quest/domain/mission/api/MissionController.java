package com.idoncare.quest.domain.mission.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.idoncare.quest.domain.mission.application.service.MissionService;
import com.idoncare.quest.domain.mission.dto.req.CheckMissionReq;
import com.idoncare.quest.domain.mission.dto.req.MissionIdReq;
import com.idoncare.quest.domain.mission.dto.req.MissionReq;
import com.idoncare.quest.domain.mission.dto.req.ModifyMissionReq;
import com.idoncare.quest.domain.mission.dto.resp.MissionDetailResp;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.global.common.CommonResponse;
import com.idoncare.quest.global.common.ErrorCode;
import com.idoncare.quest.global.exception.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Tag(name = "Mission API", description = "미션와 관련된 api")
@Slf4j
@RestController
@RequestMapping("/mission")
public class MissionController {

	private final MissionService missionService;

	public MissionController(MissionService missionService) {
		this.missionService = missionService;
	}

	@Operation(summary = "main page 미션 리스트 반환 [완료]", description = "접속한 유저의 자기자신/자식의 미션 리스트 반환 (최대 2개)")
	@PostMapping("/list")
	public ResponseEntity<CommonResponse<?>> getMissionList(@RequestBody @Valid RelationIdReq relationIdReq) {
		log.info("========== 최근 미션 리스트반환 시작 ==========");
		List<MissionDetailResp> missionList = missionService.getMissionList(relationIdReq);
		log.info("========== 최근 미션 리스트반환 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(missionList));
	}

	@Operation(summary = "전체 미션 중 진행중인 미션 반환 [완료]", description = "접속한 유저의 자기자신/자식의 전체 미션 중 진행중인 미션 반환")
	@PostMapping("/listIn")
	public ResponseEntity<CommonResponse<?>> getMissionInProgessList(@RequestBody @Valid RelationIdReq relationIdReq) {
		log.info("========== 진행중인 미션 리스트반환 시작 ==========");
		List<MissionDetailResp> missionList = missionService.getMissionInProgessList(relationIdReq);
		log.info("========== 진행중인 리스트반환 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(missionList));
	}

	@Operation(summary = "전체 미션 중 완료한 미션 반환 [완료]", description = "접속한 유저의 자기자신/자식의 전체 미션 중 완료한 미션 반환")
	@PostMapping("/listCom")
	public ResponseEntity<CommonResponse<?>> getMissionCompleteList(@RequestBody @Valid RelationIdReq relationIdReq) {
		log.info("========== 완료한 미션 리스트반환 시작 ==========");
		List<MissionDetailResp> missionList = missionService.getMissionCompleteList(relationIdReq);
		log.info("========== 완료한 리스트반환 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(missionList));
	}

	@Operation(summary = "미션 등록 [완료]", description = "자식에게 미션 등록")
	@PostMapping("/regist")
	public ResponseEntity<CommonResponse<?>> registMission(@RequestBody @Valid final MissionReq missionReq) {
		missionService.registMission(missionReq);
		return ResponseEntity.ok(CommonResponse.from(null));
	}

	@Operation(summary = "미션 수정 [완료]", description = "등록된 미션 수정")
	@PutMapping("/modify")
	public ResponseEntity<CommonResponse<?>> modifyMission(@RequestBody @Valid final ModifyMissionReq modifyMissionReq) {
		log.info("========== 미션 수정 시작 ==========");
		missionService.modifyMission(modifyMissionReq);
		log.info("========== 미션 수정 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}

	@Operation(summary = "미션 삭제 [완료]", description = "등록된 미션 삭제")
	@PutMapping("/cancel")
	public ResponseEntity<CommonResponse<?>> deleteMission(@RequestBody @Valid final MissionIdReq missionIdReq) {
		log.info("========== 미션 수정 시작 ==========");
		missionService.deleteMission(missionIdReq);
		log.info("========== 미션 수정 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}

	@Operation(summary = "미션 제출 [완료]", description = "미션과 관련된 이미지를 제출")
	@PostMapping(value = "/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CommonResponse<?>> reviewMission(@RequestPart("image") MultipartFile image,
		@RequestPart("missionId") final String missionId) {
		log.info("========== 미션 제출 시작 ==========");
		if (image.isEmpty()) {
			throw new NotFoundException(ErrorCode.Q004);
		}
		missionService.reviewMission(image, missionId);
		log.info("========== 미션 제출 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}

	@Operation(summary = "미션 검사 완료 [완료]", description = "제출한 미션의 이미지를 보고 미션의 성공 여부 등록")
	@PutMapping("/check")
	public ResponseEntity<?> checkMission(@RequestBody @Valid final CheckMissionReq checkMissionReq) {
		log.info("========== 미션 검사 시작 ==========");
		missionService.checkMission(checkMissionReq);
		log.info("========== 미션 검사 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}
}