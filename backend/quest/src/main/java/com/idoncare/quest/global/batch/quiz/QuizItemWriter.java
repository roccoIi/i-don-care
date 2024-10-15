package com.idoncare.quest.global.batch.quiz;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.idoncare.quest.domain.quiz.dto.QuizDtoImpl;

@Component
public class QuizItemWriter implements ItemWriter<QuizDtoImpl> {

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public void write(Chunk<? extends QuizDtoImpl> items) {
		for (QuizDtoImpl quizDtoImp : items) {
			redisTemplate.opsForValue().set(String.valueOf(quizDtoImp.getLevel()), quizDtoImp);
		}
	}
}
