package com.exercise.carrotproject.domain.member.repository;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberJdbcRepository {
    private final JdbcTemplate jdbcTemplate;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Transactional
    public void mannerScoreUpdateAll(List<MemberDto> memberDtos) {
        int batchCount = 0;
        List<MemberDto> subMemberDtos = new ArrayList<>();
        for (int i = 0; i < memberDtos.size(); i++) {
            subMemberDtos.add(memberDtos.get(i));
            if ((i + 1) % batchSize == 0) {
                batchCount = mannerScoreUpdate(batchCount, subMemberDtos);
            }
        }
        // 나머지 subMemberDtos를 update
        if (!subMemberDtos.isEmpty()) {
            batchCount = mannerScoreUpdate(batchCount, subMemberDtos);
        }
        System.out.println("batchCount: " + batchCount);
    }

    @Transactional
    public int mannerScoreUpdate(int batchCount, List<MemberDto> subMemberDtos) {
        this.jdbcTemplate.batchUpdate(
                "UPDATE member SET manner_score = manner_score + ? WHERE mem_id = ?",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MemberDto memberDto = subMemberDtos.get(i);
                        ps.setDouble(1, memberDto.getMannerScore());
                        ps.setString(2, memberDto.getMemId());
                        System.out.println("memberDto.getMemId() = update? " + memberDto.getMemId());
                    }
                    public int getBatchSize() {
                        return subMemberDtos.size();
                    }
                });
        subMemberDtos.clear();
        batchCount++;
        return batchCount;
    }
}
