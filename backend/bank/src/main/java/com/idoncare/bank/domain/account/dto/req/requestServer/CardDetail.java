package com.idoncare.bank.domain.account.dto.req.requestServer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class CardDetail {
	private String cardNo;
	private String cvc;
	private String startDate;
	private String endDate;

	public static CardDetail from(String cardNo, String cvc){
		return CardDetail.builder()
			.cardNo(cardNo)
			.cvc(cvc)
			.startDate("20240401")
			.endDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
			.build();
	}
}
