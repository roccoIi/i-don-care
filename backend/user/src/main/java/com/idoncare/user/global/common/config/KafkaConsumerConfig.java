package com.idoncare.user.global.common.config;

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

import com.idoncare.user.dto.kafka.BalanceDto;
import com.idoncare.user.dto.kafka.RelationDto;
import com.idoncare.user.dto.kafka.RelationMissionDto;
import com.idoncare.user.dto.kafka.RelationQuizDto;
import com.idoncare.user.dto.kafka.RelationSavingsDto;
import com.idoncare.user.dto.kafka.RelationSavingsProgressDto;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	/**
	 * default factory
	 * @return
	 */
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

	/**
	 * mission 완료 + mission 제출
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, RelationMissionDto> missionConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RelationMissionDto.class.getName());
		configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
		configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(configProps);
	}


	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RelationMissionDto>> missionKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, RelationMissionDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(missionConsumerFactory());
		return factory;
	}

	/**
	 * quiz 완료
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, RelationQuizDto> quizConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RelationQuizDto.class.getName());
		configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
		configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(configProps);
	}


	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RelationQuizDto>> quizKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, RelationQuizDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(quizConsumerFactory());
		return factory;
	}

	/**
	 * savings 완료
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, RelationSavingsDto> savingsConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RelationSavingsDto.class.getName());
		configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
		configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RelationSavingsDto>> savingsKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, RelationSavingsDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(savingsConsumerFactory());
		return factory;
	}

	/**
	 * savings 시작
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, RelationDto> savingsStartConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RelationDto.class.getName());
		configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
		configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RelationDto>> savingsStartKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, RelationDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(savingsStartConsumerFactory());
		return factory;
	}

	/**
	 * savings 진행 (저축)
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, RelationSavingsProgressDto> savingsProgressConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RelationSavingsProgressDto.class.getName());
		configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
		configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RelationSavingsProgressDto>> savingsProgressKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, RelationSavingsProgressDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(savingsProgressConsumerFactory());
		return factory;
	}

	/**
	 * 잔액 부족
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, BalanceDto> balanceConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, BalanceDto.class.getName());
		configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
		configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, BalanceDto>> balanceKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, BalanceDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(balanceConsumerFactory());
		return factory;
	}

}
// RelationSavingsProgressDto
