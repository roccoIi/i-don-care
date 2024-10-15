package com.idoncare.quest.domain.quiz.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idoncare.quest.domain.quiz.application.service.QuizService;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.domain.quiz.dto.req.ReviewQuizReq;
import com.idoncare.quest.domain.quiz.dto.resp.QuizResp;
import com.idoncare.quest.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Tag(name = "Quiz API", description = "퀴즈와 관련된 api")
@Slf4j
@RestController
@RequestMapping("/quiz")
public class QuizController {

	private final QuizService quizService;

	public QuizController(QuizService quizService) {
		this.quizService = quizService;
	}

	@Operation(summary = "퀴즈 문제 조회 [완료]", description = "오늘의 퀴즈를 반환")
	@PostMapping("/question")
	public ResponseEntity<CommonResponse<?>> getQuiz(@RequestBody @Valid RelationIdReq relationIdReq) {
		log.info("========== 퀴즈 조회 시작 ==========");
		QuizResp quiz = quizService.getQuiz(relationIdReq.getRelationId());
		log.info("========== 퀴즈 조회 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(quiz));
	}

	@Operation(summary = "퀴즈 답안 제출 [완료]", description = "오늘의 퀴즈 문제에 해당하는 답안 제출")
	@PostMapping("/reviewQuiz")
	public ResponseEntity<CommonResponse<?>> reviewQuiz(@RequestBody @Valid ReviewQuizReq reviewQuizReq) {
		log.info("========== 답안 제출 시작 ==========");
		quizService.reviewQuiz(reviewQuizReq);
		log.info("========== 답안 제출 완료 ==========");
		return ResponseEntity.ok(CommonResponse.from(null));
	}
}