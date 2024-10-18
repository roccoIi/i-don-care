package com.idoncare.user.domain.notification;

import com.idoncare.user.domain.enums.NotificationType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Notification {

	private Long notificationId;
	private NotificationType type;
	private Long senderId; // 보내는사람
	private String senderName;
	private Long receiverId; // 받는사람
	private Boolean isRead;

}
