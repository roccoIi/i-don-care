package com.idoncare.user.dto.resp;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRelationViewResp {
	private Long allowanceId = 1L;
	private Long coinboxId = 1L;
	private Long missionId = 1L;
	private List<String> interest = Arrays.asList("", "", "");
}
