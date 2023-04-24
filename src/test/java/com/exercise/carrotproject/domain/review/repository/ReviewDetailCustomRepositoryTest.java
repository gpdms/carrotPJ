package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@SpringBootTest
class ReviewDetailCustomRepositoryTest {
    @Autowired
    ReviewDetailCustomRepository reviewDetailCustomRepository;

    @Test
    public void unionAllTest(){
        Map<ReviewIndicator, Long> mannerDetail = reviewDetailCustomRepository.getMannerDetailsBySqlQueryFactory("tester2", "P");
        System.out.println("ReviewDetailCustomRepositoryTest.unionAllTest");
        for (Map.Entry<ReviewIndicator, Long> entry : mannerDetail.entrySet()) {
            System.out.println("entry.getKey() = " + entry.getKey());
            System.out.println("entry.getValue() = " + entry.getValue());
        }
    }

    @Test
    @Transactional
    public void unionAllNativeQueryTest(){
        Map<ReviewIndicator, Long> mannerDetails = reviewDetailCustomRepository.getMannerDetails("tester2", "P");
        for (Map.Entry<ReviewIndicator, Long> entry : mannerDetails.entrySet()) {
            System.out.println("entry.getKey() = " + entry.getKey());
            System.out.println("entry.getValue() = " + entry.getValue());
        }
    }

    @Test
    @Transactional
    public void unionAllJPASQLQueryTest(){
        reviewDetailCustomRepository.getMannerDetailsByJPASQLQuery("tester2", "P");
    }

    @Test
    @Transactional
    public void test2() {
        List<String> mannerDetail2 = reviewDetailCustomRepository.getMannerDetail2();
//        for (ReviewSellerIndicator reviewSellerIndicator : mannerDetail2) {
//            String name = reviewSellerIndicator.name();
//            System.out.println("name = " + name);
//        }
    }
    @Test
    @Transactional
    public void test3() {
        List<String> mannerDetail3 = reviewDetailCustomRepository.getMannerDetail3();
        System.out.println("mannerDetail3 = " + mannerDetail3);
        for (String memId: mannerDetail3) {
            System.out.println("memId = " + memId);
        }
    }

}