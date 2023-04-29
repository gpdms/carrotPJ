package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewBuyerCustomRepositoryTest {
    @Autowired
    ReviewBuyerCustomRepository reviewBuyerCustomRepository;

    @Test
    void test1 () {
        List<MemberDto> memberDtos = reviewBuyerCustomRepository.sumScoresForUpdateMannerScore();
        for (MemberDto memberDto : memberDtos) {
            System.out.println("memberDto = " + memberDto);
        }
    }

}