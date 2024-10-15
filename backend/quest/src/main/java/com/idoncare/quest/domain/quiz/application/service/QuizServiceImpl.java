package com.idoncare.quest.domain.quiz.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.quest.domain.notification.application.service.NotificationProducerService;
import com.idoncare.quest.domain.notification.dto.req.QuizNotiReq;
import com.idoncare.quest.domain.quiz.dto.QuizDtoImpl;
import com.idoncare.quest.domain.quiz.dto.QuizSolvedDto;
import com.idoncare.quest.domain.quiz.dto.req.ReviewQuizReq;
import com.idoncare.quest.domain.quiz.dto.resp.QuizResp;
import com.idoncare.quest.domain.quiz.repository.QuizRepository;
import com.idoncare.quest.global.common.ErrorCode;
import com.idoncare.quest.global.exception.NotFoundException;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private RedisTemplate<String, QuizDtoImpl> redisTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private QuizRepository quizRepository;
	private final NotificationProducerService notificationProducerService;

	public QuizServiceImpl(NotificationProducerService notificationProducerService, QuizRepository quizRepository) {
		this.notificationProducerService = notificationProducerService;
		this.quizRepository = quizRepository;
	}

	@Override
	public QuizResp getQuiz(Long relationId) {
		// User 풀이 여부 확인
		QuizSolvedDto quizSolvedDto = quizRepository.getUserInfo(relationId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.Q000));

		QuizResp quizResp = null;

		Long level = calcurateLevel(quizSolvedDto.getPreviousQuizRating());
		Object redisValue = redisTemplate.opsForValue().get(String.valueOf(level));
		QuizDtoImpl quiz = objectMapper.convertValue(redisValue, QuizDtoImpl.class);

		quizResp = QuizResp.builder()
			.relationId(relationId)
			.isSolved(quizSolvedDto.getIsSolved())
			.quizId(quiz.getQuizId())
			.question(quiz.getQuestion())
			.answer(quiz.getAnswer())
			.description(quiz.getDescription())
			.level(level)
			.build();
		return quizResp;
	}

	@Override
	public void reviewQuiz(ReviewQuizReq reviewQuizReq) {
		// 맞췄을 경우 점수 증가, 부모 -> 자식 계좌로 이체
		if (reviewQuizReq.getRealAnswer().equals(reviewQuizReq.getUserAnswer())) {
			quizRepository.reviewQuiz(reviewQuizReq.getRelationId(), 2L);
			QuizNotiReq quizNotiReq = new QuizNotiReq(reviewQuizReq.getRelationId(), 100L, true);
			notificationProducerService.sendQuizNotification("quiz-complete", quizNotiReq);
		} else { // 틀렸을 경우 점수 감소
			quizRepository.reviewQuiz(reviewQuizReq.getRelationId(), -1L);
			QuizNotiReq quizNotiReq = new QuizNotiReq(reviewQuizReq.getRelationId(), 0L, false);
			notificationProducerService.sendQuizNotification("quiz-complete", quizNotiReq);
		}
	}

	public Long calcurateLevel(Long rating) {
		if (rating >=120) {
			return 5L;
		} else if (rating >=90) {
			return 4L;
		} else if (rating >=60) {
			return 3L;
		} else if (rating >=30) {
			return 2L;
		} else {
			return 1L;
		}
	}
}
