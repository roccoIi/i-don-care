package com.idoncare.quest.global.batch;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimeBasedBatchScheduler {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job QuizJob;

	@Autowired
	private Job AllowanceJob;

	@Scheduled(cron = "0 */5 * * * *")
	public void runDailyQuizJob() throws Exception {
		jobLauncher.run(QuizJob, new JobParametersBuilder()
			.addDate("date", new Date())
			.toJobParameters());
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void runDailyAllowanceJob() throws Exception {
		jobLauncher.run(AllowanceJob, new JobParametersBuilder()
			.addDate("date", new Date())
			.toJobParameters());
	}
}
