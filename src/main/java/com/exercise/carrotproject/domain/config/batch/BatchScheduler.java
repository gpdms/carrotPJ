package com.exercise.carrotproject.domain.config.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 0 5 * * *") //매일 새벽 5시
    public void runMannerScoreBatchUpdate() {
        //job paramter설정
        Map<String, JobParameter> jobParametersMap = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String date = format.format(new Date());
        jobParametersMap.put("date", new JobParameter(date));
        JobParameters jobParameters = new JobParameters(jobParametersMap);
        try {
            JobExecution mannerScoreUpdateJob
                    = jobLauncher.run(jobRegistry.getJob("mannerScoreUpdateJob"), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | JobRestartException | NoSuchJobException e) {
            log.error(e.getMessage());
        }
    }
}
