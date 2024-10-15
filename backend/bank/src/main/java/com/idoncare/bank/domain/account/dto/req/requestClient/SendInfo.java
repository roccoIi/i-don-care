package com.idoncare.bank.domain.account.dto.req.requestClient;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SendInfo {
	@Builder.Default
	private String sendAccountNum = null;
	private String receiveAccountNum;
	private Long amount;
}
