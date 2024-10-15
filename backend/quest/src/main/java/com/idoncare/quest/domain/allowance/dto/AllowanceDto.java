package com.idoncare.quest.domain.allowance.dto;

import java.time.LocalDateTime;

public interface AllowanceDto {
	Long getAllowanceId();
	Long getRelationId();
	Long getType();
	Long getDay();
	Long getAmount();
	Boolean getDeleteYn();
	LocalDateTime getCreatedAt();
}
