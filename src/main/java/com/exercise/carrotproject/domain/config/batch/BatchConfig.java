package com.exercise.carrotproject.domain.config.batch;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    public JobBuilderFactory jobBuilderFactory;

    public StepBuilderFactory stepBuilderFactory;




}
