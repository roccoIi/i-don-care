package com.idoncare.quest.domain.mission.dto;

import java.time.LocalDateTime;

public interface MissionDto {
	Long getMissionId();
	String getState();
	String getTitle();
	String getContent();
	Long getAmount();
	String getProofPictureUrl();
	LocalDateTime getInputDate();
	Long getRelationId();
	Boolean getDeleteYn();
	LocalDateTime getCreateAt();
}
