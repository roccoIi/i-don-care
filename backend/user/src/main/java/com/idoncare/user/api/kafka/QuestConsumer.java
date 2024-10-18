package com.idoncare.user.api.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.idoncare.user.application.notification.NotificationService;
import com.idoncare.user.application.relation.RelationService;
import com.idoncare.user.domain.enums.NotificationType;
import com.idoncare.user.domain.mapper.KafkaMapper;
import com.idoncare.user.domain.mapper.NotificationMapper;
import com.idoncare.user.domain.relation.Relation;
import com.idoncare.user.dto.kafka.RelationDto;
import com.idoncare.user.dto.kafka.RelationMissionDto;
import com.idoncare.user.dto.kafka.RelationQuizDto;
import com.idoncare.user.dto.kafka.RelationSavingsDto;
import com.idoncare.user.dto.kafka.RelationSavingsProgressDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuestConsumer {

	@Autowired
	private BankProducer bankProducer;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private RelationService relationService;

	@KafkaListener(topics = "mission-complete", groupId = "user-group",
		containerFactory = "missionKafkaListenerContainerFactory")
	public void listenMissionComplete(RelationMissionDto relationMissionDto) {
		log.info("미션 완료 알림 수신 {}", relationMissionDto);
		try {
			Relation relation = relationService.findParentAndChild(relationMissionDto.getRelationId());

			bankProducer.sendMissionNotification(
				KafkaMapper.fromRelationToUserOfMission(relationMissionDto, relation)
			);

		} catch (Exception e) {
			log.error("미션 완료알림 처리 중 에러 발생: {}", e.getMessage(), e);
		}
	}

	@KafkaListener(topics = "mission-review", groupId = "user-group",
		containerFactory = "missionKafkaListenerContainerFactory")
	public void listenMissionReview(RelationMissionDto relationMissionDto) {
		log.info("미션 제출 알림 수신 {}", relationMissionDto);
		try {
			Relation relation = relationService.findParentAndChild(relationMissionDto.getRelationId());

			notificationService.saveNotification(
				NotificationMapper.fromDomainToDomain(
					relation.getChild(),
					relation.getParent(),
					NotificationType.MISSION_SUCCESS
				)
			);

		} catch (Exception e) {
			log.error("미션 제출 알림 처리 중 에러 발생: {}", e.getMessage(), e);
		}
	}

	@KafkaListener(topics = "quiz-complete", groupId = "user-group",
		containerFactory = "quizKafkaListenerContainerFactory")
	public void listenQuizComplete(RelationQuizDto relationQuizDto) {
		log.info("퀴즈 완료 알림 수신 {}", relationQuizDto);
		try {
			Relation relation = relationService.findParentAndChild(relationQuizDto.getRelationId());

			notificationService.saveNotification(
				NotificationMapper.fromDomainToDomain(
					relation.getChild(),
					relation.getParent(),
					NotificationType.QUIZ_SUCCESS
				)
			);

			bankProducer.sendQuizNotification(
				KafkaMapper.fromRelationToUserOfQuiz(relationQuizDto, relation)
			);
		} catch (Exception e) {
			log.error("퀴즈 완료알림 처리 중 에러 발생: {}", e.getMessage(), e);
		}
	}

	@KafkaListener(topics = "savings-complete", groupId = "user-group",
		containerFactory = "savingsKafkaListenerContainerFactory")
	public void listenSavingsComplete(RelationSavingsDto relationSavingsDto) {
		log.info("저금 완료 알림 수신 {}", relationSavingsDto);

		try {
			Relation relation = relationService.findParentAndChild(relationSavingsDto.getRelationId());
			notificationService.saveNotification(
				NotificationMapper.fromDomainToDomain(
					relation.getChild(),
					relation.getParent(),
					NotificationType.COINBOX_SUCCESS
				)
			);
			bankProducer.sendSavingsNotification(
				KafkaMapper.fromRelationToUserOfSavings(relationSavingsDto, relation)
			);
		} catch (Exception e) {
			log.error("저금 완료알림 처리 중 에러 발생: {}", e.getMessage(), e);
		}

	}

	@KafkaListener(topics = "savings-complete", groupId = "user-group",
		containerFactory = "savingsKafkaListenerContainerFactory")
	public void listenSavingsCancel(RelationSavingsDto relationSavingsDto) {
		log.info("저금 취소 알림 수신 {}", relationSavingsDto);

		try {
			Relation relation = relationService.findParentAndChild(relationSavingsDto.getRelationId());
			bankProducer.sendSavingsNotification(
				KafkaMapper.fromRelationToUserOfSavings(relationSavingsDto, relation)
			);
		} catch (Exception e) {
			log.error("저금 취소 알림 처리 중 에러 발생: {}", e.getMessage(), e);
		}

	}

	@KafkaListener(topics = "savings-regist-complete", groupId = "user-group",
		containerFactory = "savingsStartKafkaListenerContainerFactory")
	public void listenSavingsStartComplete(RelationDto relationDto) {
		log.info("저금 시작 알림 수신 {}", relationDto);
		try {
			Relation relation = relationService.findParentAndChild(relationDto.getRelationId());
			notificationService.saveNotification(
				NotificationMapper.fromDomainToDomain(
					relation.getChild(),
					relation.getParent(),
					NotificationType.COINBOX_START
				)
			);
		} catch (Exception e) {
			log.error("저금 시작알림 처리 중 에러 발생: {}", e.getMessage(), e);
		}
	}

	@KafkaListener(topics = "savings-progress", groupId = "user-group",
		containerFactory = "savingsProgressKafkaListenerContainerFactory")
	public void listenSavingsProgress(RelationSavingsProgressDto progressDto) {
		log.info("저축 알림 수신 {}", progressDto);
		try {
			Relation relation = relationService.findParentAndChild(progressDto.getRelationId());
			bankProducer.sendSavingsProgressNotification(
				KafkaMapper.fromRelationToUserOfSavings(progressDto, relation)
			);
		} catch (Exception e) {
			log.error("저금 시작알림 처리 중 에러 발생: {}", e.getMessage(), e);
		}
	}
}
