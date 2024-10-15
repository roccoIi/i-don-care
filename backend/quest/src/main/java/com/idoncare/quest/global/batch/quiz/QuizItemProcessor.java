package com.idoncare.quest.global.batch.quiz;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.idoncare.quest.domain.quiz.dto.QuizDto;
import com.idoncare.quest.domain.quiz.dto.QuizDtoImpl;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QuizItemProcessor implements ItemProcessor<QuizDto, QuizDtoImpl> {

	@Override
	public QuizDtoImpl process(QuizDto quizDto) {
		log.info("============== Quiz History Process START =============");
		QuizDtoImpl quizDtoImpl = new QuizDtoImpl(quizDto);
		log.info("============== Quiz History Process END =============");
		return quizDtoImpl;
	}
}
