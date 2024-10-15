package com.idoncare.quest.domain.notification.application.service;

import com.idoncare.quest.domain.notification.dto.req.AllowanceNotiReq;
import com.idoncare.quest.domain.notification.dto.req.QuizNotiReq;
import com.idoncare.quest.domain.notification.dto.req.MissionNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsCancelNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsProgressNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsRegistNotiReq;

public interface NotificationConsumerService {
	void listen(String notification);

	void missionReviewNotilisten(MissionNotiReq missionNotiReq);

	void missionNotilisten(MissionNotiReq missionNotiReq);

	void quizNotilisten(QuizNotiReq quizNotiReq);

	void savingsNotilisten(SavingsNotiReq savingsNotiReq);

	void savingsProgressNotilisten(SavingsProgressNotiReq savingsProgressNotiReq);

	void savingsRegistNotilisten(SavingsRegistNotiReq savingsRegistNotiReq);

	void savingsCancelNotilisten(SavingsCancelNotiReq savingsCancelNotiReq);

	void allowanceNotilisten(AllowanceNotiReq allowanceNotiReq);
}
