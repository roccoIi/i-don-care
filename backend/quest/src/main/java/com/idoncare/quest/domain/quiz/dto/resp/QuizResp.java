package com.idoncare.quest.domain.quiz.dto.resp;

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
public class QuizResp {
	private Long relationId;
	private Boolean isSolved;
	private Long quizId;
	private String question;
	private String answer;
	private String description;
	private Long level;

	@Override
	public String toString() {
		return "QuizResp{" +
			"relationId=" + relationId +
			", isSolved=" + isSolved +
			", quizId=" + quizId +
			", question='" + question + '\'' +
			", answer='" + answer + '\'' +
			", description='" + description + '\'' +
			", level=" + level +
			'}';
	}
}
