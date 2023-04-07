package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.review.dto.BuyerDetailCountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewBuyerDetailCustomRepositoryTest {
    @Autowired
    ReviewBuyerDetailCustomRepository reviewBuyerDetailCustomRepository;
    @Transactional
    @Test
    void test1() {
        List<BuyerDetailCountDto> buyerDetailCountDto = reviewBuyerDetailCustomRepository.countIndicatorByBuyer("tester2");
        for (BuyerDetailCountDto detailCountDto : buyerDetailCountDto) {
            System.out.println("detailCountDto = " + detailCountDto);
        }
    }
}