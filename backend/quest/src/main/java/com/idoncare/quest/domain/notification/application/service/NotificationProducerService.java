package com.idoncare.quest.domain.notification.application.service;

import java.util.List;

import com.idoncare.quest.domain.allowance.dto.AllowanceDto;
import com.idoncare.quest.domain.notification.dto.req.AllowanceNotiReq;
import com.idoncare.quest.domain.notification.dto.req.MissionNotiReq;
import com.idoncare.quest.domain.notification.dto.req.NotificationReq;
import com.idoncare.quest.domain.notification.dto.req.QuizNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsCancelNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsProgressNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsRegistNotiReq;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public interface NotificationProducerService {
	void testNotification(NotificationReq notificationReq);

	void sendMissionNotification(String topic, MissionNotiReq missionNotiReq);

	void sendQuizNotification(String topic, QuizNotiReq quizNotiReq);

	void sendSavingsNotification(String topic, SavingsNotiReq savingsNotiReq);

	void sendSavingsProgressNotification(String topic, SavingsProgressNotiReq savingsProgressNotiReq);

	void sendSavingsRegistNotification(String topic, SavingsRegistNotiReq savingsRegistNotiReq);

	void sendSavingsCancelNotification(String topic, SavingsCancelNotiReq savingsCancelNotiReq);

	void sendAllowanceNotification(String topic, AllowanceNotiReq allowanceNotiReq);
}
