package com.idoncare.quest.domain.openfeign.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingsCheckDto {
	private String code;
	private String message;
	private Balance data;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Balance{
		private Long balance;
	}
}
