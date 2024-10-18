package com.idoncare.user.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.user.application.notification.NotificationManager;
import com.idoncare.user.dto.req.NotiUpdateReq;
import com.idoncare.user.dto.resp.NotificationListResp;
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
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name = "NOTIFICATION API", description = "알림을 위한 API")
@Validated
public class NotificationController {

	@Autowired
	private NotificationManager notificationManager;

	@GetMapping
	@Operation(summary = "[완료] 알림 조회", description = "알림 조회")
	public ResponseEntity<?> getNotificationList(HttpServletRequest request) {
		log.info("===알림 조회 START===");
		String userIdHeader = request.getHeader("X-User-Id");
		if (userIdHeader == null) {
			throw new AuthorizedException(ErrorCode.A009);  // 인증 실패 처리
		}
		long userId = Long.parseLong(userIdHeader);
		List<NotificationListResp> resp = notificationManager.getNotifications(userId);
		log.info("===알림 조회 END===");
		return ResponseEntity.ok(CommonResponse.from(resp));
	}

	@PostMapping
	@Operation(summary = "[완료] 알림 읽음 처리", description = "유저가 알림을 읽었을 때, 읽음 처리로 업데이트")
	public ResponseEntity<?> updateNotificationOnRead(@RequestBody @Valid final NotiUpdateReq req) {
		log.info("===알림 읽음 처리 START===");
		notificationManager.updateNotificationOnRead(req);
		log.info("===알림 읽음 처리 END===");
		return ResponseEntity.ok(new CommonResponse<>());
	}

	@GetMapping("/count")
	@Operation(summary = "[완료] 알림 개수 읽기", description = "알림 개수를 리턴")
	public ResponseEntity<?> getListNotifications(HttpServletRequest request) {
		log.info("===알림 개수 읽기 처리 START===");
		String userIdHeader = request.getHeader("X-User-Id");
		if (userIdHeader == null) {
			throw new AuthorizedException(ErrorCode.A009);  // 인증 실패 처리
		}
		long userId = Long.parseLong(userIdHeader);
		int resp = notificationManager.getListNotifications(userId);
		log.info("===알림 개수 읽기 처리 END===");
		return ResponseEntity.ok(CommonResponse.from(resp));
	}

}
