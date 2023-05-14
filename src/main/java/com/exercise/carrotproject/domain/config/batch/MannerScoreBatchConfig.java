package com.exercise.carrotproject.domain.config.batch;


import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.repository.MannerScoreRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class MannerScoreBatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobRegistry jobRegistry;
    private final ReviewCustomRepository reviewCustomRepository;
    private final MannerScoreRepository mannerScoreRepository;

    @Bean
    public Job job() {
        Job job = jobBuilderFactory.get("mannerScoreUpdateJob")
                .start(step1(null))
                .next(step2(null))
                .build();
        return job;
    }

    @Bean
    @JobScope
    public Step step1(@Value("#{jobParameters[date]}") String date) {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    List<MemberDto> memberDtos = reviewCustomRepository.sumScoreForUpdateMannerScore();
                    //test
                    /*MemberDto tester2 = MemberDto.builder().mannerScore(5000000.0).memId("tester2").build();
                    MemberDto tester3 = MemberDto.builder().mannerScore(15000.0).memId("tester3").build();
                    List<MemberDto> memberDtos = new ArrayList<>(Arrays.asList(tester3));*/
                    mannerScoreRepository.updateMannerScore(memberDtos);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step step2(@Value("#{jobParameters[date]}") String date) {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    long result = mannerScoreRepository.updateMannerScoreDown();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    /**
     * JobRegistryBeanPostProcessor
     */
    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry); // 빈 셋팅
        return postProcessor;
    }
}
