package com.idoncare.quest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
public class QuestApplication {

	public static void main(String[] args) throws InterruptedException {
		// Thread.sleep(5000);
		SpringApplication.run(QuestApplication.class, args);
	}

}
