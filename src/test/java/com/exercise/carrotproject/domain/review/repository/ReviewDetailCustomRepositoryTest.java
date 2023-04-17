package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;


@SpringBootTest
class ReviewDetailCustomRepositoryTest {
    @Autowired
    ReviewDetailCustomRepository reviewDetailCustomRepository;

    @Test
    public void unionAllTest(){
        Map<ReviewIndicator, Long> mannerDetail = reviewDetailCustomRepository.getMannerDetail("tester3", "P");
        System.out.println("ReviewDetailCustomRepositoryTest.unionAllTest");
        for (Map.Entry<ReviewIndicator, Long> entry : mannerDetail.entrySet()) {
            System.out.println("entry.getKey() = " + entry.getKey());
            System.out.println("entry.getValue() = " + entry.getValue());
        }
    }
}