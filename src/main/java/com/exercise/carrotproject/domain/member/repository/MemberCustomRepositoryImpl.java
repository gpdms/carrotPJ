package com.exercise.carrotproject.domain.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.exercise.carrotproject.domain.member.entity.QBlock.block;
import static com.exercise.carrotproject.domain.member.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public String selectNicknameByMemId(String memId) {
        return jpaQueryFactory.select(member.nickname).from(member)
                .where(member.memId.eq(memId))
                .fetchFirst();
    }
    public boolean hasBlockByMemIds(String memId1, String memId2) {
        BooleanExpression condition1 = block.fromMem.memId.eq(memId1).and(
                block.toMem.memId.eq(memId2));
        BooleanExpression condition2 = block.fromMem.memId.eq(memId2).and(
                block.toMem.memId.eq(memId1));
        Integer result = jpaQueryFactory.selectOne().from(block)
                .where(condition1.or(condition2))
                .fetchFirst();
        return result != null? true : false;
    }


}
