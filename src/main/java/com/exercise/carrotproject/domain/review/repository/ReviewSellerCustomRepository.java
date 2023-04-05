package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewSellerCustomRepository {
    private final JPAQueryFactory queryFactory;
}
