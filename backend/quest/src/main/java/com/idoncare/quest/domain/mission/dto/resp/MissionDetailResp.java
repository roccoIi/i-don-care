package com.idoncare.quest.domain.mission.dto.resp;

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
public class MissionDetailResp {
	private Long missionId;
	private Long relationId;
	private String state;
	private String title;
	private String content;
	private Long amount;
	private String proofPictureUrl;
}
