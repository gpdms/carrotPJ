package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.SellState;

import com.exercise.carrotproject.domain.member.entity.QMember;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.QSoldPostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Post;

import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
//QPost를 static import함으로써, QPost에 미리 정의된 Q 타입 인스턴스 상수를 사용
import static com.exercise.carrotproject.domain.member.entity.QBlock.block;
import static com.exercise.carrotproject.domain.member.entity.QMember.member;
import static com.exercise.carrotproject.domain.post.entity.QMtPlace.mtPlace;
import static com.exercise.carrotproject.domain.post.entity.QPost.post;
import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{
    private final JPAQueryFactory jpaQueryFactory;

/*    @Override
    public long updateHideState(Long postId){
        return jpaQueryFactory
                .update(post)
                .set(post.hideState, HideState.HIDE)
                .where(post.postId.eq(postId))
                .execute();
    }*/

    @Override
    public List<SoldPostDto> getSoldPostListByMemId(String memId) {
        return jpaQueryFactory
                .select(new QSoldPostDto(post, reviewBuyer.reviewBuyerId))
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        post.sellState.eq(SellState.SOLD),
                        post.member.memId.eq(memId))
                .leftJoin(reviewBuyer)
                .on(reviewBuyer.post.postId.eq(post.postId))
                .leftJoin(post.mtPlace, mtPlace)
                .fetchJoin()
                .orderBy(post.postId.desc())
                .fetch();
    }

    @Override
    public Page<PostDto> getOnSalePostPageByMemId(String memId, Pageable pageable) {
        List<PostDto> onSalePosts = getOnSalePostListByMemId(memId, pageable);
        Long count = getOnSalePostCount(memId);
        return new PageImpl<>(onSalePosts, pageable, count);
    }

    private List<PostDto> getOnSalePostListByMemId(String memId, Pageable pageable) {
        List<Post> postList = jpaQueryFactory
                .select(post)
                .from(post)
                .where(post.member.memId.eq(memId),
                        post.hideState.eq(HideState.SHOW),
                        post.sellState.ne(SellState.SOLD))
                .orderBy(post.postId.desc())
                .leftJoin(post.mtPlace, mtPlace)
                .fetchJoin()
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        return PostEntityDtoMapper.toDtoList(postList);
    }

    private Long getOnSalePostCount(String memId) {
        return jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        post.sellState.ne(SellState.SOLD),
                        post.member.memId.eq(memId))
                .orderBy(post.postId.desc())
                .fetchOne();
    }

    @Override
    public Page<SoldPostDto> getSoldPostPageByMemId(String memId, Pageable pageable) {
        List<SoldPostDto> content = getSoldPostListByMemId(memId, pageable);
        Long count = getSoldPostCount(memId);
        return new PageImpl<>(content, pageable, count);
    }

    private List<SoldPostDto> getSoldPostListByMemId(String memId, Pageable pageable) {
        return jpaQueryFactory
                .select(new QSoldPostDto(post, reviewBuyer.reviewBuyerId))
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        post.sellState.eq(SellState.SOLD),
                        post.member.memId.eq(memId))
                .leftJoin(reviewBuyer)
                .on(reviewBuyer.post.postId.eq(post.postId))
                .leftJoin(post.mtPlace, mtPlace)
                .fetchJoin()
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    private Long getSoldPostCount(String memId) {
        return jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        post.sellState.eq(SellState.SOLD),
                        post.member.memId.eq(memId))
                .leftJoin(reviewBuyer)
                .on(reviewBuyer.post.postId.eq(post.postId))
                .fetchOne();
    }

    @Override
    public Page<PostDto> getHiddenPostPageByMemId(String memId, Pageable pageable) {
        List<PostDto> content = getHiddenPostListByMemId(memId, pageable);
        Long count = getHiddenPostCount(memId);
        return new PageImpl<>(content, pageable, count);
    }

    private List<PostDto> getHiddenPostListByMemId(String memId, Pageable pageable) {
        List<Post> postList = jpaQueryFactory
                .select(post)
                .from(post)
                .where(post.member.memId.eq(memId),
                        post.hideState.eq(HideState.HIDE))
                .orderBy(post.postId.desc())
                .leftJoin(post.mtPlace, mtPlace)
                .fetchJoin()
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        return PostEntityDtoMapper.toDtoList(postList);
    }

    private Long getHiddenPostCount(String memId) {
        return jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(post.member.memId.eq(memId),
                        post.hideState.eq(HideState.HIDE))
                .orderBy(post.postId.desc())
                .fetchOne();
    }

    @Override
    public List<Post> getMyPostListByLimit(String memId, int limit) {
        return jpaQueryFactory.select(post)
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        memIdEq(memId))
                .limit(limit)
                .leftJoin(post.mtPlace, mtPlace)
                .fetchJoin()
                .orderBy(post.postId.desc())
                .fetch();
    }

    @Override
    public List<Post> getPostListByLimit(String memId, Loc loc, int limit) {
        List<Long> ids = jpaQueryFactory.select(post.postId)
                .from(post)
                .where(post.hideState.eq(HideState.SHOW),
                        notExistsBlock(memId))
                .orderBy(post.postId.desc())
                .limit(limit)
                .fetch();

        if(CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        return jpaQueryFactory.select(post)
                .from(post)
                .where(post.postId.in(ids))
                .leftJoin(post.mtPlace, mtPlace)
                .fetchJoin()
                .innerJoin(post.member, member)
                .fetchJoin()
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
                .leftJoin(post.mtPlace, mtPlace)
                .fetchJoin()
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
                .leftJoin(post.mtPlace, mtPlace)
                .fetchJoin()
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
                .leftJoin(post.mtPlace, mtPlace)
                .fetchJoin()
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
