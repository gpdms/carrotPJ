package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
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

    public List<Post> selectBoardPost(Member member){
        //hidestate, block, loc,sellstate 고려
        String jpql = "select p from Post p " +
                "where p.loc = :loc " +
                "and p.hideState = :hideState " +
                "and p.sellState <> :sellState " +
                "and not exists (select b from Block b " +
                    "where b.toMem = :member and b.fromMem = p.member.memId " +
                    "or b.toMem =p.member.memId and b.fromMem=:member) " +
                "order by p.createdTime desc";

        Query query = em.createQuery(jpql, Post.class);
        query.setParameter("loc", member.getLoc());
        query.setParameter("hideState", HideState.SHOW);
        query.setParameter("sellState", SellState.SOLD);
        query.setParameter("member", member);

        List<Post> postEntityList = query.getResultList();
        return postEntityList;
    }

/*    public List<Post> searchPost(String searchWord, String loginMemId){
        //hidestate, sellstate, block 고려
        String jpql = "select p from Post p " +
                "where p.loc = :loc " +
                "and p.hideState = :hideState " +
                "and p.sellState <> :sellState " +
                "and not exists (select b from Block b " +
                "where b.toMem = :member and b.fromMem = p.member.memId " +
                "or b.toMem =p.member.memId and b.fromMem=:member) " +
                "order by p.createdTime desc";

        BooleanExpression exists =JPAExpressions.select(block)
                .from(block)
                .where(fromIdEq(loginMemId).
                        toIdEq())
                .exists();
        jpaQueryFactory.select(post)
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        post.sellState.eq(SellState.ON_SALE),
                        post.title.like("%"+searchWord+"%"),

    }

    public BooleanExpression fromIdEq(String loginMemId) {
        return hasText(loginMemId) ? block.fromMem.memId.eq(loginMemId) : null;
    }
    public BooleanExpression toIdEq(String loginMemId) {
        return hasText(loginMemId) ? block.toMem.memId.eq(loginMemId) : null;
    }*/

}
