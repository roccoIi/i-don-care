package com.idoncare.quest.domain.quiz.dto.req;

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
public class ReviewQuizReq {
	@PositiveOrZero
	@NotNull
	private Long relationId;

	@NotNull
	private String realAnswer;

	@NotNull
	private String userAnswer;
}
