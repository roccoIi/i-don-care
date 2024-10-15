package com.idoncare.quest.domain.quiz.dto;

public interface QuizSolvedDto {
	Long getSolvedId();
	Boolean getIsSolved();
	Long getPreviousQuizRating();
	Long getRelationId();
}
