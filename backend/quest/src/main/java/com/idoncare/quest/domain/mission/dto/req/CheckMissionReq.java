package com.idoncare.quest.domain.mission.dto.req;

import com.idoncare.quest.domain.mission.entity.MissionStateType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckMissionReq {
	@PositiveOrZero
	@NotNull
	private Long missionId;
}
