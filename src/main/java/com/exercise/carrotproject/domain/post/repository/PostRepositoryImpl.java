package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.SellState;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.QBlock;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.QSoldPostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.QReviewBuyer;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

//QPost를 static import함으로써, QPost에 미리 정의된 Q 타입 인스턴스 상수를 사용
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.exercise.carrotproject.domain.member.entity.QBlock.block;
 import static com.exercise.carrotproject.domain.post.entity.QPost.post;
import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository{
    @PersistenceContext
    EntityManager em;
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

    public List<Post> selectBoardPost(String loginMemId, Loc loginMemLoc){
        return jpaQueryFactory.select(post)
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        locEq(loginMemLoc),
                        notExistsBlock(loginMemId)
                )
                .orderBy(post.postId.desc())
                .fetch();
    }
    public List<Post> selectBoardPostByCategory(String loginMemId, Loc loginMemLoc, Category category){
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
    public List<Post> postListByLimit(int limit, String memId) {
        return jpaQueryFactory.select(post)
                        .from(post)
                        .where(post.hideState.eq(HideState.SHOW),
                                memIdEq(memId))
                        .limit(limit)
                        .orderBy(post.postId.desc())
                        .fetch();
    }
    public BooleanExpression notExistsBlock(String loginMemId) {
        BooleanExpression booleanExpression = null;
        if(hasText(loginMemId)) {
            BooleanExpression fromIdEq= block.fromMem.memId.eq(loginMemId).and(
                    block.toMem.memId.eq(post.member.memId));
            BooleanExpression toIdEq = block.toMem.memId.eq(loginMemId).and(
                    block.fromMem.memId.eq(post.member.memId));
            booleanExpression = JPAExpressions.selectFrom(block)
                    .where(fromIdEq.or(toIdEq))
                    .exists().not();
        }
        return booleanExpression;
    }
    public BooleanExpression locEq(Loc loc){
        return loc != null ? post.loc.eq(loc) : null;
    }
    public BooleanExpression memIdEq(String memId){
        return hasText(memId) ? post.member.memId.eq(memId) : null;
    }

}
