package com.exercise.carrotproject;

import com.exercise.carrotproject.domain.member.ouath.kakao.KaKaoOauth;
import com.exercise.carrotproject.domain.member.ouath.naver.NaverOauth;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
@EnableConfigurationProperties({KaKaoOauth.class, NaverOauth.class})
public class CarrotProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarrotProjectApplication.class, args);
    }
}
