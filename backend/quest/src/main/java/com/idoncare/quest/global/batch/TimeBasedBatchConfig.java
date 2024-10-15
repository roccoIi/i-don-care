package com.idoncare.quest.global.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.idoncare.quest.domain.allowance.dto.AllowanceDto;
import com.idoncare.quest.domain.allowance.dto.AllowanceDtoImpl;
import com.idoncare.quest.domain.allowance.repository.AllowanceRepository;
import com.idoncare.quest.domain.notification.application.service.NotificationProducerService;
import com.idoncare.quest.domain.notification.dto.req.AllowanceNotiReq;
import com.idoncare.quest.domain.quiz.dto.QuizDto;
import com.idoncare.quest.domain.quiz.dto.QuizDtoImpl;
import com.idoncare.quest.domain.quiz.repository.QuizRepository;
import com.idoncare.quest.global.batch.quiz.QuizItemProcessor;
import com.idoncare.quest.global.batch.quiz.QuizItemReader;
import com.idoncare.quest.global.batch.quiz.QuizItemWriter;

@Configuration
@EnableBatchProcessing
public class TimeBasedBatchConfig {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private QuizItemReader quizItemReader;

	@Autowired
	private QuizItemProcessor quizItemProcessor;

	@Autowired
	private QuizItemWriter quizItemWriter;

	private final QuizRepository quizRepository;
	private final AllowanceRepository allowanceRepository;
	private final NotificationProducerService notificationProducerService;

	public TimeBasedBatchConfig(QuizRepository quizRepository, AllowanceRepository allowanceRepository,
		NotificationProducerService notificationProducerService) {
		this.quizRepository = quizRepository;
		this.allowanceRepository = allowanceRepository;
		this.notificationProducerService = notificationProducerService;
	}

	@Bean
	public Job QuizJob() {
		return new JobBuilder("QuizJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(updateRatingStep())
			.next(QuizStep())
			.build();
	}

	@Bean
	public Step updateRatingStep() {
		return new StepBuilder("updateScoreStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				quizRepository.updateRating(); // 점수 업데이트 로직 호출
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.transactionManager(transactionManager) // 트랜잭션 관리 필요
			.build();
	}

	@Bean
	public Step QuizStep() {
		return new StepBuilder("QuizStep", jobRepository)
			.<QuizDto, QuizDtoImpl>chunk(5, transactionManager)
			.reader(quizItemReader)
			.processor(quizItemProcessor)
			.writer(quizItemWriter)
			.build();
	}

	@Bean
	public Job AllowanceJob() {
		return new JobBuilder("AllowanceJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(updateAllowanceStep())
			.build();
	}

	@Bean
	public Step updateAllowanceStep() {
		return new StepBuilder("updateScoreStep", jobRepository)
			.tasklet(updateAllowanceTasklet(), transactionManager)
			.transactionManager(transactionManager) // 트랜잭션 관리 필요
			.build();
	}

	@Bean
	public Tasklet updateAllowanceTasklet() {
		return (contribution, chunkContext) -> {
			List<AllowanceDto> allowanceDtos = allowanceRepository.dailyUpdate();
			List<AllowanceDtoImpl> allowanceDtoImpls = allowanceDtos.stream()
				.map(dto -> new AllowanceDtoImpl(dto))
				.collect(Collectors.toList());
			AllowanceNotiReq allowanceNotiReq = new AllowanceNotiReq(allowanceDtoImpls);
			notificationProducerService.sendAllowanceNotification("allowance-complete", allowanceNotiReq);
			return RepeatStatus.FINISHED;
		};
	}
}
