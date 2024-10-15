package com.idoncare.bank.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.idoncare.bank.global.exception.decoder.FeignErrorDecoder;

@Configuration
public class FeignConfiguration {

	@Bean
	public FeignErrorDecoder feignErrorDecoder() {
		return new FeignErrorDecoder();
	}
}
