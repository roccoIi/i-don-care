package com.idoncare.quest.domain.savings.dto;

import java.time.LocalDateTime;

public interface CoinboxDto {
	Long getCoinboxId();
	String getGoalTitle();
	Long getGoalAmount();
	Long getAmount();
	Long getInterestAmount();
	Long getRelationId();
	Boolean getDeleteYn();
	LocalDateTime getCreateDate();
}
