package com.idoncare.bank.domain.account.application.kafka;

import com.idoncare.bank.global.common.ErrorCode;
import com.idoncare.bank.global.exception.JsonParsingException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;

	// 주입받은 KafkaTemplate을 사용하여 Kafka에 메시지를 전송하는 send 메서드
	public void send(String topic, Object sendInfo){
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			// OrderDto 객체를 JSON 문자열로 직렬화
			jsonInString = mapper.writeValueAsString(sendInfo);
		} catch(JsonProcessingException e) {
			throw new JsonParsingException(ErrorCode.G003);
		}

		// KafkaTemplate을 사용하여 지정된 토픽에 JSON 문자열을 전송
		kafkaTemplate.send(topic, jsonInString);
	}
}

// 로깅을 통해 전송된 데이터를 기록
//log.info("Kafka Producer send data " + sendInfo);

// 전송된 OrderDto 객체를 반환
// return sendInfo;

