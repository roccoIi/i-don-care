package com.idoncare.quest.domain.mission.dto.req;

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
public class MissionIdReq {
	@PositiveOrZero
	@NotNull
	private Long missionId;

	@Override
	public String toString() {
		return "MissionIdReq{" +
			"missionId=" + missionId +
			'}';
	}
}
