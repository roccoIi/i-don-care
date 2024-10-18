package com.idoncare.user.api.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.idoncare.user.application.notification.NotificationService;
import com.idoncare.user.application.user.UserService;
import com.idoncare.user.domain.enums.NotificationType;
import com.idoncare.user.domain.mapper.NotificationMapper;
import com.idoncare.user.domain.user.User;
import com.idoncare.user.dto.kafka.BalanceDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BankConsumer {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserService userService;

	@KafkaListener(topics = "less-balance", groupId = "user-group",
		containerFactory = "balanceKafkaListenerContainerFactory")
	public void listenLessBalance(BalanceDto balanceDto) {
		log.info("출금 잔액 부족 알람 {}", balanceDto);
		User user = userService.findUser(balanceDto.getUserId());

		notificationService.saveNotification(
			NotificationMapper.fromDomainToDomain(
				user,
				NotificationType.LESS_BALANCE
			)
		);
	}

}
