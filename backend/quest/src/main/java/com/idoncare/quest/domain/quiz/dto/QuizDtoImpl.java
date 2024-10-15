package com.idoncare.quest.domain.quiz.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class QuizDtoImpl implements QuizDto {
	private Long quizId;
	private String question;
	private String answer;
	private String description;
	private Long level;
	private LocalDate date;
	private Boolean deleteYn;
	private LocalDateTime createdAt;

	public QuizDtoImpl(QuizDto quizDto) {
		this.quizId = quizDto.getQuizId();
		this.question = quizDto.getQuestion();
		this.answer = quizDto.getAnswer();
		this.description = quizDto.getDescription();
		this.level = quizDto.getLevel();
		this.date = quizDto.getDate();
		this.deleteYn = quizDto.getDeleteYn();
		this.createdAt = quizDto.getCreatedAt();
	}
}
