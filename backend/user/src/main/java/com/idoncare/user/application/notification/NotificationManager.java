package com.idoncare.user.application.notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idoncare.user.domain.mapper.NotificationMapper;
import com.idoncare.user.dto.req.NotiUpdateReq;
import com.idoncare.user.dto.resp.NotificationListResp;

@Component
public class NotificationManager {

	@Autowired
	private NotificationService notificationService;

	public List<NotificationListResp> getNotifications(long receiverId) {
		return notificationService.getNotifications(receiverId)
			.stream()
			.map(NotificationMapper::fromDomainToDto)
			.toList();
	}

	public void updateNotificationOnRead(NotiUpdateReq req) {
		notificationService.updateOnRead(req.getNotificationId());
	}

	public int getListNotifications(long userId) {
		return notificationService.getNotifications(userId).size();
	}
}
