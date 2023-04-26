package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.SellState;
import com.exercise.carrotproject.domain.post.dto.QSoldPostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.review.entity.QReviewBuyer;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

//QPost를 static import함으로써, QPost에 미리 정의된 Q 타입 인스턴스 상수를 사용
import java.util.List;

import static com.exercise.carrotproject.domain.post.entity.QPost.post;
import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository{
    private final JPAQueryFactory jpaQueryFactory;
//    @Override
    public long updateHideState(Long postId){
        return jpaQueryFactory
                .update(post)
                .set(post.hideState, HideState.HIDE)
                .where(post.postId.eq(postId))
                .execute();
    }
//findByMemberAndSellStateAndHideStateOrderByPostIdDesc
    public List<SoldPostDto> getSoldList(String memId) {
         return jpaQueryFactory
                .select(new QSoldPostDto(post, reviewBuyer.reviewBuyerId))
                .from(post)
                .where(post.member.memId.eq(memId),
                        post.hideState.eq(HideState.SHOW),
                        post.sellState.eq(SellState.SOLD))
                .leftJoin(reviewBuyer)
                .on(reviewBuyer.post.postId.eq(post.postId))
                .fetch();
    }
}
