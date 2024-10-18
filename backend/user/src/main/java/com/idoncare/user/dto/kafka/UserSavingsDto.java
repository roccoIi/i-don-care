package com.idoncare.user.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserSavingsDto {

	private Long parentId;

	private Long childId;

	private Long amount;

	private Long interestAmount;

	private boolean review;

}
