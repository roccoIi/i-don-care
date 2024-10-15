package com.idoncare.quest.domain.notification.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.idoncare.quest.domain.allowance.dto.AllowanceDto;
import com.idoncare.quest.domain.notification.dto.req.AllowanceNotiReq;
import com.idoncare.quest.domain.notification.dto.req.QuizNotiReq;
import com.idoncare.quest.domain.notification.dto.req.MissionNotiReq;
import com.idoncare.quest.domain.notification.dto.req.NotificationReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsCancelNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsProgressNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsRegistNotiReq;

@Service
public class NotificationProducerServiceImpl implements NotificationProducerService {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final KafkaTemplate<String, MissionNotiReq> kafkaMissionNotiTemplate;
	private final KafkaTemplate<String, QuizNotiReq> kafkaQuizNotiTemplate;
	private final KafkaTemplate<String, SavingsNotiReq> kafkaSavingsNotiTemplate;
	private final KafkaTemplate<String, SavingsProgressNotiReq> kafkaSavingsProgressTemplate;
	private final KafkaTemplate<String, SavingsRegistNotiReq> kafkaSavingsRegistTemplate;
	private final KafkaTemplate<String, SavingsCancelNotiReq> kafkaSavingsCancelTemplate;
	private final KafkaTemplate<String, AllowanceNotiReq> kafkaAllowanceNotiTemplate;

	public NotificationProducerServiceImpl(KafkaTemplate<String, String> kafkaTemplate,
		KafkaTemplate<String, MissionNotiReq> kafkaMissionNotiTemplate,
		KafkaTemplate<String, QuizNotiReq> kafkaQuizNotiTemplate,
		KafkaTemplate<String, SavingsNotiReq> kafkaSavingsNotiTemplate,
		KafkaTemplate<String, SavingsProgressNotiReq> kafkaSavingsProgressTemplate,
		KafkaTemplate<String, SavingsRegistNotiReq> kafkaSavingsRegistTemplate,
		KafkaTemplate<String, SavingsCancelNotiReq> kafkaSavingsCancelTemplate,
		KafkaTemplate<String, AllowanceNotiReq> kafkaAllowanceNotiTemplate) {
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaMissionNotiTemplate = kafkaMissionNotiTemplate;
		this.kafkaQuizNotiTemplate = kafkaQuizNotiTemplate;
		this.kafkaSavingsNotiTemplate = kafkaSavingsNotiTemplate;
		this.kafkaSavingsProgressTemplate = kafkaSavingsProgressTemplate;
		this.kafkaSavingsRegistTemplate = kafkaSavingsRegistTemplate;
		this.kafkaAllowanceNotiTemplate = kafkaAllowanceNotiTemplate;
		this.kafkaSavingsCancelTemplate = kafkaSavingsCancelTemplate;
	}

	@Override
	public void testNotification(NotificationReq notificationReq) {
		kafkaTemplate.send(notificationReq.getTopic(), notificationReq.getNotification());
	}

	@Override
	public void sendMissionNotification(String topic, MissionNotiReq missionNotiReq) {
		kafkaMissionNotiTemplate.send(topic, missionNotiReq);
	}

	@Override
	public void sendQuizNotification(String topic, QuizNotiReq quizNotiReq) {
		kafkaQuizNotiTemplate.send(topic, quizNotiReq);
	}

	@Override
	public void sendSavingsNotification(String topic, SavingsNotiReq savingsNotiReq) {
		kafkaSavingsNotiTemplate.send(topic, savingsNotiReq);
	}

	@Override
	public void sendSavingsProgressNotification(String topic, SavingsProgressNotiReq savingsProgressNotiReq) {
		kafkaSavingsProgressTemplate.send(topic, savingsProgressNotiReq);
	}

	@Override
	public void sendSavingsRegistNotification(String topic, SavingsRegistNotiReq savingsRegistNotiReq) {
		kafkaSavingsRegistTemplate.send(topic, savingsRegistNotiReq);
	}

	@Override
	public void sendSavingsCancelNotification(String topic, SavingsCancelNotiReq savingsCancelNotiReq) {
		kafkaSavingsCancelTemplate.send(topic, savingsCancelNotiReq);
	}

	@Override
	public void sendAllowanceNotification(String topic, AllowanceNotiReq allowanceNotiReq) {
		kafkaAllowanceNotiTemplate.send(topic, allowanceNotiReq);
	}
}
