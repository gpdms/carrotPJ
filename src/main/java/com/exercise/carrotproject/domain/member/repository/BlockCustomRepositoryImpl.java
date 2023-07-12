package com.exercise.carrotproject.domain.member.repository;

import com.exercise.carrotproject.domain.member.dto.MyBlockDto;
import com.exercise.carrotproject.domain.member.dto.QMannerUpdateDto;
import com.exercise.carrotproject.domain.member.dto.QMyBlockDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.exercise.carrotproject.domain.member.entity.QBlock.block;
import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BlockCustomRepositoryImpl implements BlockCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public boolean existsBlockByFromMemToMem(String fromMemId, String toMemId) {
        Integer result = jpaQueryFactory.selectOne().from(block)
                .where(block.fromMem.memId.eq(fromMemId),
                        block.toMem.memId.eq(toMemId))
                .fetchFirst();
        return result != null ? true : false;
    }

    public boolean existsBlockByMemIds(String memId1, String memId2) {
        BooleanExpression mem1BlocksMem2 = block.fromMem.memId.eq(memId1).and(
                block.toMem.memId.eq(memId2));
        BooleanExpression mem2BlocksMem1 = block.fromMem.memId.eq(memId2).and(
                block.toMem.memId.eq(memId1));
        Integer result = jpaQueryFactory.selectOne().from(block)
                .where(mem1BlocksMem2.or(mem2BlocksMem1))
                .fetchFirst();
        return result != null ? true : false;
    }

    public List<MyBlockDto> findBlocksByFromMemId(String fromMemId) {
        return jpaQueryFactory.select(
                        new QMyBlockDto(block.blockId, block.toMem))
                .from(block)
                .where(block.fromMem.memId.eq(fromMemId))
                .orderBy(block.createdTime.desc())
                .fetch();
    }
}
