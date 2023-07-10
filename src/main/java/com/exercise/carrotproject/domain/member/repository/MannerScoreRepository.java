package com.exercise.carrotproject.domain.member.repository;

import com.exercise.carrotproject.domain.member.dto.MannerUpdateDto;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.exercise.carrotproject.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class  MannerScoreRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private final JdbcTemplate jdbcTemplate;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}") //사이즈 만큼씩 업데이트
    private int batchSize;

    @Transactional
    public void updateMannerScore(List<MannerUpdateDto> memberDtos) {
        int batchCount = 0;
        List<MannerUpdateDto> subMemberDtos = new ArrayList<>();
        for (int i = 0; i < memberDtos.size(); i++) {
            subMemberDtos.add(memberDtos.get(i));
            if ((i + 1) % batchSize == 0) {
                batchCount = updateMannerScoreSub(batchCount, subMemberDtos);
            }
        }
        if (!subMemberDtos.isEmpty()) {
            batchCount = updateMannerScoreSub(batchCount, subMemberDtos);
        }
    }
    @Transactional
    public int updateMannerScoreSub(int batchCount, List<MannerUpdateDto> subMemberDtos) {
        LocalDateTime monday5am = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(5).withMinute(0).withSecond(0).withNano(0);
        Timestamp updatedTimeManner = Timestamp.valueOf(monday5am.minusDays(7));
        this.jdbcTemplate.batchUpdate(
                "UPDATE member SET manner_score = manner_score + ?," +
                        " updated_time_manner = ?" +
                        " WHERE mem_id = ?" +
                        " and manner_score + ? between 0 and 1200000",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MannerUpdateDto memberDto = subMemberDtos.get(i);
                        ps.setDouble(1, memberDto.getReviewScore());
                        ps.setTimestamp(2, updatedTimeManner);
                        ps.setString(3, memberDto.getMemId());
                        ps.setDouble(4, memberDto.getReviewScore());
                    }
                    public int getBatchSize() {
                        return subMemberDtos.size();
                    }
                });
        subMemberDtos.clear();
        batchCount++;
        return batchCount;
    }

    //1. 최근 활동 없으면 매너온도 0.25 down (36.5도 까지만)
    //최근 받은 리뷰가 없어 매너온도 업데이트가 되지 않았다면, 최근 활동이 없는 것.
    @Transactional
    public void updateMannerScoreDown() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monday5am = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(5).withMinute(0).withSecond(0).withNano(0);
        Timestamp updatedTimeManner = Timestamp.valueOf(monday5am);
        Timestamp nowTime = Timestamp.valueOf(now);
        jpaQueryFactory.update(member)
                .set(member.mannerScore, 365000.0)
                .where(member.updatedTimeManner.notBetween(updatedTimeManner, nowTime),
                        member.mannerScore.lt(367500),
                        member.mannerScore.gt(365000))
                .execute();
        jpaQueryFactory.update(member)
                .set(member.mannerScore, member.mannerScore.subtract(2500))
                .where(member.updatedTimeManner.notBetween(updatedTimeManner, nowTime),
                        member.mannerScore.goe(367500))
                .execute();
    }

}

