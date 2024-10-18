package com.idoncare.user.dto.resp;

import java.util.Arrays;
import java.util.List;

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
public class UserRelationViewResp {
	private Long allowanceId = 1L;
	private Long coinboxId = 1L;
	private Long missionId = 1L;
	private List<String> interest = Arrays.asList("", "", "");
}
