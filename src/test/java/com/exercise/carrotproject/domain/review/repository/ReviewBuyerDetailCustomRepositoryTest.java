package com.exercise.carrotproject.domain.review.repository;

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
}