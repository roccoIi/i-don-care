package com.idoncare.quest.domain.quiz.application.service;

import com.idoncare.quest.domain.quiz.dto.req.ReviewQuizReq;
import com.idoncare.quest.domain.quiz.dto.resp.QuizResp;

import jakarta.validation.Valid;

public interface QuizService {
	QuizResp getQuiz(Long relationId);

	void reviewQuiz(@Valid ReviewQuizReq reviewQuizReq);
}
