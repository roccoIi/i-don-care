package com.idoncare.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(5000);
		SpringApplication.run(UserApplication.class, args);
	}

}
