package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.post.entity.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TradeCustomRepositoryTest {
    @Autowired
    TradeCustomRepository tradeCustomRepository;

    @Test
    public void joinTest(){
        List<Trade> buyList = tradeCustomRepository.getBuyList("tester3");
        System.out.println("buyList = " + buyList);
    }
}