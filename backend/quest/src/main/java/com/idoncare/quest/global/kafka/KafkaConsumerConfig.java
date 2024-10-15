package com.idoncare.quest.global.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.idoncare.quest.domain.notification.dto.req.AllowanceNotiReq;
import com.idoncare.quest.domain.notification.dto.req.QuizNotiReq;
import com.idoncare.quest.domain.notification.dto.req.MissionNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsCancelNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsProgressNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsRegistNotiReq;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, MissionNotiReq> missionConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MissionNotiReq.class.getName());
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MissionNotiReq>> missionKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, MissionNotiReq> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(missionConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, QuizNotiReq> quizConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, QuizNotiReq.class.getName());
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, QuizNotiReq>> quizKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, QuizNotiReq> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(quizConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, SavingsNotiReq> savingsConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, SavingsNotiReq.class.getName());
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, SavingsNotiReq>> savingsKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, SavingsNotiReq> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(savingsConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, SavingsProgressNotiReq> savingsProgressConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, SavingsProgressNotiReq.class.getName());
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, SavingsProgressNotiReq>> savingsProgressKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, SavingsProgressNotiReq> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(savingsProgressConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, SavingsRegistNotiReq> savingsRegistConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, SavingsRegistNotiReq.class.getName());
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, SavingsRegistNotiReq>> savingsRegistKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, SavingsRegistNotiReq> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(savingsRegistConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, SavingsCancelNotiReq> savingsCancelConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, SavingsCancelNotiReq.class.getName());
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, SavingsCancelNotiReq>> savingsCancelKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, SavingsCancelNotiReq> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(savingsCancelConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, AllowanceNotiReq> allowanceConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, AllowanceNotiReq.class.getName());
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, AllowanceNotiReq>> allowanceKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, AllowanceNotiReq> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(allowanceConsumerFactory());
		return factory;
	}
}
