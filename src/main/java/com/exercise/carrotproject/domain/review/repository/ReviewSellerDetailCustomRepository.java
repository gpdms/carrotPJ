package com.exercise.carrotproject.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewSellerDetailCustomRepository {
    private final JPAQueryFactory queryFactory;
}
