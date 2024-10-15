package com.idoncare.bank.global.exception.decoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.bank.global.exception.OpenFeignException;

import feign.Response;
import feign.codec.ErrorDecoder;
//TODO: 유레카 서버 올라갔을 때 활성화 시킬 것
//import jakarta.ws.rs.InternalServerErrorException;


public class FeignErrorDecoder implements ErrorDecoder {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Exception decode(String methodKey, Response response) {

		SsafyErrorResponse errorResponse = new SsafyErrorResponse();

		try (InputStream inputStream = response.body().asInputStream();
			 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
		){
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}

			String responseBody = stringBuilder.toString();
			errorResponse = objectMapper.readValue(responseBody, SsafyErrorResponse.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (response.status()) {
			case 400 :
				return new OpenFeignException(errorResponse);
			case  500 :
				//TODO: 유레카 서버 올라갔을 때 활성화 시킬 것
//				return new InternalServerErrorException("SSAFY API 서버 내부에 오류가 발생했습니다.");
		}
		return new Exception(response.reason());
	}
}
