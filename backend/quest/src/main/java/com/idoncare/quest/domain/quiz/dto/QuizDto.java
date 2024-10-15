package com.idoncare.quest.domain.quiz.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface QuizDto {
	Long getQuizId();
	String getQuestion();
	String getAnswer();
	String getDescription();
	Long getLevel();
	LocalDate getDate();
	Boolean getDeleteYn();
	LocalDateTime getCreatedAt();
}
