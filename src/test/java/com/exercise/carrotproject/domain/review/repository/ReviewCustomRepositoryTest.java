package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewCustomRepositoryTest {
    @Autowired
    ReviewCustomRepository reviewCustomRepository;
/*    @Test
    void testSumScore () {
        List<Object[]> sumScore = reviewCustomRepository.getSumScore();
        for(Object[] row : sumScore){
            String mem_id = (String)row[0];
            Double manner_score = (Double) row[1];
            System.out.println("mem_id !!= " + mem_id);
            System.out.println("manner_score!! = " + manner_score);
        }
    }*/
}