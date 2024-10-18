package com.idoncare.user.domain.mapper;

import java.time.LocalDateTime;

import com.idoncare.user.domain.enums.NotificationType;
import com.idoncare.user.domain.notification.Notification;
import com.idoncare.user.domain.user.User;
import com.idoncare.user.dto.resp.NotificationListResp;
import com.idoncare.user.entity.NotificationEntity;

public class NotificationMapper {

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

	public static NotificationEntity createEntity(Notification domain) {
		return NotificationEntity.builder()
			.notificationId(domain.getNotificationId())
			.type(domain.getType())
			.senderId(domain.getSenderId())
			.senderName(domain.getSenderName())
			.receiverId(domain.getReceiverId())
			.isRead(false)
			.deleteYn(false)
			.createAt(LocalDateTime.now())
			.build();
	}

	public static Notification fromDomainToDomain(User sender, User receiver, NotificationType type) {
		return Notification.builder()
			.senderId(sender.getUserId())
			.senderName(sender.getUserName())
			.receiverId(receiver.getUserId())
			.type(type)
			.build();
	}

	public static Notification fromDomainToDomain(User parent, NotificationType type) {
		return Notification.builder()
			.senderId(parent.getUserId()) // sender가 없으므로, 임의로 본인 id
			.senderName(parent.getUserName()) // sender가 없으므로, 임의로 본인 name
			.receiverId(parent.getUserId())
			.type(type)
			.build();
	}

	public static NotificationEntity toEntity(Notification domain) {
		return NotificationEntity.builder()
			.notificationId(domain.getNotificationId())
			.type(domain.getType())
			.senderId(domain.getSenderId())
			.senderName(domain.getSenderName())
			.receiverId(domain.getReceiverId())
			.isRead(domain.getIsRead())
			.build();
	}

	public static Notification fromEntity(NotificationEntity entity) {
		return Notification.builder()
			.notificationId(entity.getNotificationId())
			.type(entity.getType())
			.senderId(entity.getSenderId())
			.senderName(entity.getSenderName())
			.receiverId(entity.getReceiverId())
			.isRead(entity.getIsRead())
			.build();
	}

	public static NotificationListResp fromDomainToDto(Notification domain) {
		return NotificationListResp.builder()
			.notificationId(domain.getNotificationId())
			.senderId(domain.getSenderId())
			.senderName(domain.getSenderName())
			.type(domain.getType())
			.isRead(domain.getIsRead())
			.build();
	}

}
