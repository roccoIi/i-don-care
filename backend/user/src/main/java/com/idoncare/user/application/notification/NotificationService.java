package com.idoncare.user.application.notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idoncare.user.domain.mapper.NotificationMapper;
import com.idoncare.user.domain.notification.Notification;
import com.idoncare.user.entity.NotificationEntity;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.exception.NotFoundException;
import com.idoncare.user.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	NotificationRepository notificationRepository;

	@Transactional(readOnly = true)
	public List<Notification> getNotifications(long receiverId) {
		return notificationRepository.findByReceiverId(receiverId)
			.stream()
			.map(NotificationMapper::toDomain)
			.toList();
	}

	@Transactional
	public Notification saveNotification(Notification notification) {
		NotificationEntity notificationEntity = notificationRepository.save(
			NotificationMapper.createEntity(notification)
		);
		return NotificationMapper.toDomain(notificationEntity);
	}

	@Transactional
	public void updateOnRead(Long notificationId) {
		NotificationEntity notificationEntity = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.N001));
		notificationEntity.updateReadIsTrue();
	}

}
