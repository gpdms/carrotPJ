package com.exercise.carrotproject.domain.review.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReviewBuyerCustomRepositoryTest {
    @Autowired
    ReviewBuyerCustomRepositoryImpl reviewBuyerCustomRepositoryImpl;

/*    @Test
    void test1 () {
        List<MemberDto> memberDtos = reviewBuyerCustomRepository.sumScoresForUpdateMannerScore();
        for (MemberDto memberDto : memberDtos) {
            System.out.println("memberDto = " + memberDto);
        }
    }*/

}