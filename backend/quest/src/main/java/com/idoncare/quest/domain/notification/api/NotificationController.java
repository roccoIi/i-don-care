package com.idoncare.quest.domain.notification.api;

import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.quest.domain.mission.dto.resp.MissionDetailResp;
import com.idoncare.quest.domain.notification.application.service.NotificationProducerService;
import com.idoncare.quest.domain.notification.dto.req.NotificationReq;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Tag(name = "Notification API", description = "알림과 관련된 api")
@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {

	private final NotificationProducerService notificationProducerService;

	public NotificationController(NotificationProducerService notificationProducerService) {
		this.notificationProducerService = notificationProducerService;
	}

	@PostMapping("/test")
	public ResponseEntity<CommonResponse<?>> testNotification(@RequestBody NotificationReq notificationReq) {
		log.info("========== kafka 메시지 전송 시작 ==========");
		notificationProducerService.testNotification(notificationReq);
		log.info("========== kafka 메시지 전송 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}
}
