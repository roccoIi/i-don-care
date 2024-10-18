package com.idoncare.user.domain.mapper;

import com.idoncare.user.domain.notification.Notification;
import com.idoncare.user.domain.relation.Relation;
import com.idoncare.user.dto.kafka.RelationMissionDto;
import com.idoncare.user.dto.kafka.RelationQuizDto;
import com.idoncare.user.dto.kafka.RelationSavingsDto;
import com.idoncare.user.dto.kafka.RelationSavingsProgressDto;
import com.idoncare.user.dto.kafka.UserMissionDto;
import com.idoncare.user.dto.kafka.UserQuizDto;
import com.idoncare.user.dto.kafka.UserSavingsDto;
import com.idoncare.user.dto.kafka.UserSavingsProgressDto;
import com.idoncare.user.entity.NotificationEntity;

public class KafkaMapper {

	public static Notification toDomain(NotificationEntity entity) {
		return Notification.builder()
			.notificationId(entity.getNotificationId())
			.type(entity.getType())
			.senderId(entity.getSenderId())
			.senderName(entity.getSenderName())
			.receiverId(entity.getReceiverId())
			.isRead(entity.getIsRead())
			.build();
	}

	public static UserMissionDto fromRelationToUserOfMission(RelationMissionDto relationMissionDto, Relation relation) {
		return UserMissionDto.builder()
			.childId(relation.getChild().getUserId())
			.parentId(relation.getParent().getUserId())
			.title(relationMissionDto.getTitle())
			.amount(relationMissionDto.getAmount())
			.review(relationMissionDto.getReview())
			.build();
	}

	public static UserQuizDto fromRelationToUserOfQuiz(RelationQuizDto relationQuizDto, Relation relation) {
		return UserQuizDto.builder()
			.childId(relation.getChild().getUserId())
			.parentId(relation.getParent().getUserId())
			.amount(relationQuizDto.getAmount())
			.review(relationQuizDto.getReview())
			.build();
	}

	public static UserSavingsDto fromRelationToUserOfSavings(RelationSavingsDto relationSavingsDto, Relation relation) {
		return UserSavingsDto.builder()
			.childId(relation.getChild().getUserId())
			.parentId(relation.getParent().getUserId())
			.interestAmount(relationSavingsDto.getInterestAmount())
			.amount(relationSavingsDto.getAmount())
			.build();
	}

	public static UserSavingsProgressDto fromRelationToUserOfSavings(RelationSavingsProgressDto progressDto, Relation relation) {
		return UserSavingsProgressDto.builder()
			.childId(relation.getChild().getUserId())
			.parentId(relation.getParent().getUserId())
			.amount(progressDto.getAmount())
			.build();
	}
}
