package com.exercise.carrotproject.domain.member.dto;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.sql.Timestamp;


@Getter
@ToString
public class MyBlockDto {
    private Long blockId;
    private ToMemberDto toMem;

    @QueryProjection
    public MyBlockDto(Long blockId, Member toMem) {
        this.blockId = blockId;
        this.toMem = ToMemberDto.builder().memId(toMem.getMemId())
                .nickname(toMem.getNickname())
                .loc(toMem.getLoc()).build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    class ToMemberDto {
        private String memId;
        private String nickname;
        private Loc loc;
    }
}
