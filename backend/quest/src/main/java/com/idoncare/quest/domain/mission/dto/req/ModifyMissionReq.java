package com.idoncare.quest.domain.mission.dto.req;

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
public class ModifyMissionReq {
	@PositiveOrZero
	@NotNull
	private Long missionId;
	@NotBlank(message = "미션 이름은 필수 입력 값입니다.")
	@NotNull
	private String title;
	@NotBlank(message = "미션 내용은 필수 입력 값입니다.")
	@NotNull
	private String content;
	@PositiveOrZero
	@NotNull
	private Long amount;
}
