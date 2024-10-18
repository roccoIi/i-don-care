package com.idoncare.user.dto.resp;

import com.idoncare.user.domain.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListResp {
	private Long notificationId;
	private Long senderId;
	private String senderName;
	private NotificationType type;
	private Boolean isRead;
}
