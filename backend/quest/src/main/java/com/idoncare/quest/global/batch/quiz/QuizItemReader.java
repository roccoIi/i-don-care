package com.idoncare.quest.global.batch.quiz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idoncare.quest.domain.quiz.dto.QuizDto;
import com.idoncare.quest.domain.quiz.repository.QuizRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QuizItemReader implements ItemReader<QuizDto> {

	@Autowired
	private QuizRepository quizRepository;

	private Iterator<QuizDto> quizIterator;

	@Override
	public QuizDto read() {
		log.info("============== Quiz History Read START =============");
		if (quizIterator == null) {
			List<QuizDto> quizDtos = quizRepository.dailyUpdate();

			LocalDateTime currentTime = LocalDateTime.now();

			// 시간 출력 포맷 지정 (Optional)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			// 포맷을 적용하여 시간 출력
			System.out.println("현재 시간: " + currentTime.format(formatter));
			System.out.println(quizDtos);

			quizIterator = quizDtos.iterator();
		}
		log.info("============== Quiz History Read END =============");
		if  (quizIterator != null && quizIterator.hasNext()) {
			return quizIterator.next();
		} else {
			quizIterator = null;
			return null;
		}
	}
}
