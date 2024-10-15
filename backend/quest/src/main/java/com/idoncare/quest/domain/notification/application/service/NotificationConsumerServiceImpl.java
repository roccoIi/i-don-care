// package com.idoncare.quest.domain.notification.application.service;
//
// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Service;
//
// import com.idoncare.quest.domain.notification.dto.req.AllowanceNotiReq;
// import com.idoncare.quest.domain.notification.dto.req.QuizNotiReq;
// import com.idoncare.quest.domain.notification.dto.req.MissionNotiReq;
// import com.idoncare.quest.domain.notification.dto.req.SavingsCancelNotiReq;
// import com.idoncare.quest.domain.notification.dto.req.SavingsNotiReq;
// import com.idoncare.quest.domain.notification.dto.req.SavingsProgressNotiReq;
// import com.idoncare.quest.domain.notification.dto.req.SavingsRegistNotiReq;
//
// @Service
// public class NotificationConsumerServiceImpl implements NotificationConsumerService {
//
// 	// @KafkaListener(topics = "${kafka.topics.test}", groupId = "${spring.kafka.consumer.group-id}")
// 	// @Override
// 	// public void listen(String notification) {
// 	// 	System.out.println("Received Message:" + notification);
// 	// }
// 	//
// 	// @KafkaListener(topics = "${kafka.topics.mission-review}", groupId = "${spring.kafka.consumer.group-id}",
// 	// 	containerFactory = "missionKafkaListenerContainerFactory")
// 	// @Override
// 	// public void missionReviewNotilisten(MissionNotiReq missionNotiReq) {
// 	// 	System.out.println("mission-review:" + missionNotiReq.toString());
// 	// }
// 	//
// 	// @KafkaListener(topics = "${kafka.topics.mission-complete}", groupId = "${spring.kafka.consumer.group-id}",
// 	// 	containerFactory = "missionKafkaListenerContainerFactory")
// 	// @Override
// 	// public void missionNotilisten(MissionNotiReq missionNotiReq) {
// 	// 	System.out.println("mission-consumer : "+missionNotiReq);
// 	// }
// 	//
// 	// @KafkaListener(topics = "${kafka.topics.quiz-complete}", groupId = "${spring.kafka.consumer.group-id}",
// 	// 	containerFactory = "quizKafkaListenerContainerFactory")
// 	// @Override
// 	// public void quizNotilisten(QuizNotiReq QuizNotiReq) {
// 	// 	System.out.println("quiz-consumer : "+QuizNotiReq);
// 	// }
// 	//
// 	// @KafkaListener(topics = "${kafka.topics.savings-complete}", groupId = "${spring.kafka.consumer.group-id}",
// 	// 	containerFactory = "savingsKafkaListenerContainerFactory")
// 	// @Override
// 	// public void savingsNotilisten(SavingsNotiReq savingsNotiReq) {
// 	// 	System.out.println("savings-consumer : "+savingsNotiReq);
// 	// }
// 	//
// 	// @KafkaListener(topics = "${kafka.topics.savings-progress}", groupId = "${spring.kafka.consumer.group-id}",
// 	// 	containerFactory = "savingsProgressKafkaListenerContainerFactory")
// 	// @Override
// 	// public void savingsProgressNotilisten(SavingsProgressNotiReq savingsProgressNotiReq) {
// 	// 	System.out.println("savings-progress : "+savingsProgressNotiReq);
// 	// }
// 	//
// 	// @KafkaListener(topics = "${kafka.topics.savings-regist-complete}", groupId = "${spring.kafka.consumer.group-id}",
// 	// 	containerFactory = "savingsRegistKafkaListenerContainerFactory")
// 	// @Override
// 	// public void savingsRegistNotilisten(SavingsRegistNotiReq savingsRegistNotiReq) {
// 	// 	System.out.println("savings-regist-consumer : "+savingsRegistNotiReq);
// 	// }
// 	//
// 	// @KafkaListener(topics = "${kafka.topics.savings-cancel}", groupId = "${spring.kafka.consumer.group-id}",
// 	// 	containerFactory = "savingsCancelKafkaListenerContainerFactory")
// 	// @Override
// 	// public void savingsCancelNotilisten(SavingsCancelNotiReq savingsCancelNotiReq) {
// 	// 	System.out.println("savings-cancel : "+savingsCancelNotiReq);
// 	// }
// 	//
// 	// @KafkaListener(topics = "${kafka.topics.allowance-complete}", groupId = "${spring.kafka.consumer.group-id}",
// 	// 	containerFactory = "allowanceKafkaListenerContainerFactory")
// 	// @Override
// 	// public void allowanceNotilisten(AllowanceNotiReq allowanceNotiReq) {
// 	// 	System.out.println("allowance-consumer : "+allowanceNotiReq);
// 	// }
// }
