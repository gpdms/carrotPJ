package com.exercise.carrotproject.domain.member.dto;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.*;


@Getter
@ToString
public class MyBlockDto {
    private Long blockId;
    private ToMemberDto toMem;

    private MyBlockDto(Long blockId, Member toMem) {
        this.blockId = blockId;
        this.toMem = new ToMemberDto(toMem.getMemId(), toMem.getNickname(), toMem.getLoc());
    }

    public static MyBlockDto of(Block block) {
        return new MyBlockDto(block.getBlockId(), block.getToMem());
    }

    @Getter
    @ToString
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ToMemberDto {
        private String memId;
        private String nickname;
        private Loc loc;
    }
}
