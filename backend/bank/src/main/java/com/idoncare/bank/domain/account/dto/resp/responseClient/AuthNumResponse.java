package com.idoncare.bank.domain.account.dto.resp.responseClient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthNumResponse {
	private String authNum;
}
