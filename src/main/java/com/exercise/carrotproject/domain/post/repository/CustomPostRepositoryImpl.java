package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.SellState;

import com.exercise.carrotproject.domain.post.dto.QSoldPostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
//QPost를 static import함으로써, QPost에 미리 정의된 Q 타입 인스턴스 상수를 사용
import static com.exercise.carrotproject.domain.member.entity.QBlock.block;
import static com.exercise.carrotproject.domain.post.entity.QPost.post;
import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public long updateHideState(Long postId){
        return jpaQueryFactory
                .update(post)
                .set(post.hideState, HideState.HIDE)
                .where(post.postId.eq(postId))
                .execute();
    }


    @Override
    public List<SoldPostDto> findSoldPostListByMemId(String memId) {
        return jpaQueryFactory
                .select(new QSoldPostDto(post, reviewBuyer.reviewBuyerId))
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        post.sellState.eq(SellState.SOLD),
                        post.member.memId.eq(memId))
                .leftJoin(reviewBuyer)
                .on(reviewBuyer.post.postId.eq(post.postId))
                .orderBy(post.postId.desc())
                .fetch();
    }

    @Override
    public List<Post> postListByLimit(String memId, int limit) {
        return jpaQueryFactory.select(post)
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        memIdEq(memId))
                .limit(limit)
                .orderBy(post.postId.desc())
                .fetch();
    }

    @Override
    public List<Post> selectPostForBoard(String loginMemId, Loc loginMemLoc){
        return jpaQueryFactory.select(post)
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        locEq(loginMemLoc),
                        notExistsBlock(loginMemId)
                )
                .orderBy(post.postId.desc())
                .fetch();
    }

    @Override
    public List<Post> selectPostForBoardByCategory(String loginMemId, Loc loginMemLoc, Category category){
        return jpaQueryFactory.select(post)
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        post.category.eq(category),
                        locEq(loginMemLoc),
                        notExistsBlock(loginMemId)
                )
                .orderBy(post.postId.desc())
                .fetch();
    }
    @Override
    public List<Post> searchPost(String loginMemId, String searchWord){
        return jpaQueryFactory.select(post)
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        post.title.like("%" + searchWord + "%").or(
                                post.content.like("%" + searchWord + "%")
                        ),
                        notExistsBlock(loginMemId)
                )
                .fetch();
    }

    private BooleanExpression notExistsBlock (String loginMemId) {
        BooleanExpression booleanExpression = null;
        if (hasText(loginMemId)) {
            BooleanExpression fromIdEq = block.fromMem.memId.eq(loginMemId)
                    .and(block.toMem.memId.eq(post.member.memId));
            BooleanExpression toIdEq = block.toMem.memId.eq(loginMemId)
                    .and(block.fromMem.memId.eq(post.member.memId));
            booleanExpression = JPAExpressions
                    .selectFrom(block)
                    .where(fromIdEq.or(toIdEq))
                    .exists().not();
        }
        return booleanExpression;
    }
    private BooleanExpression locEq(Loc loc){
        return loc != null ? post.loc.eq(loc) : null;
    }
    private BooleanExpression memIdEq(String memId){
        return hasText(memId) ? post.member.memId.eq(memId) : null;
    }
}
