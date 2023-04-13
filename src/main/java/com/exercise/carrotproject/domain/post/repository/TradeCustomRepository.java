package com.exercise.carrotproject.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TradeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public void getBuyList() {

    }
}
