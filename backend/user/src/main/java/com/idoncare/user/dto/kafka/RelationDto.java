package com.idoncare.user.dto.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RelationDto {
	@JsonProperty("relationId")
	Long relationId;

	@JsonCreator
	public RelationDto(@JsonProperty("relationId") Long relationId) {
		this.relationId = relationId;
	}
}
