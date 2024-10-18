package com.idoncare.user.api.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.idoncare.user.dto.kafka.UserMissionDto;
import com.idoncare.user.dto.kafka.UserQuizDto;
import com.idoncare.user.dto.kafka.UserSavingsDto;
import com.idoncare.user.dto.kafka.UserSavingsProgressDto;

@Service
public class BankProducer {

	@Autowired
	private KafkaTemplate<String, UserMissionDto> kafkaMissionTemplate;

	@Autowired
	private KafkaTemplate<String, UserQuizDto> kafkaQuizTemplate;

	@Autowired
	private KafkaTemplate<String, UserSavingsDto> kafkaSavingsTemplate;

	@Autowired
	private KafkaTemplate<String, UserSavingsProgressDto> kafkaSavingsProgressTemplate;

	public void sendMissionNotification(UserMissionDto userMissionDto) {
		kafkaMissionTemplate.send("mission-complete-user", userMissionDto);
	}

	public void sendQuizNotification(UserQuizDto userQuizDto) {
		kafkaQuizTemplate.send("quiz-complete-user", userQuizDto);
	}

	public void sendSavingsNotification(UserSavingsDto userSavingsDto) {
		kafkaSavingsTemplate.send("savings-complete-user", userSavingsDto);
	}

	public void sendSavingsProgressNotification(UserSavingsProgressDto progressDto) {
		kafkaSavingsProgressTemplate.send("savings-progress-user", progressDto);
	}
}
