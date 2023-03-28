package com.exercise.carrotproject.domain.member;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;

public class MemberEntityDtoMapper {
    //memberDto -> memberEntity
    public static Member toMemberEntity(MemberDto memberDto){
        return Member.builder()
                .memId(memberDto.getMemId())
                .nickname(memberDto.getNickname())
                .mannerScore(memberDto.getMannerScore())
                .loc(memberDto.getLoc())
                .build();
    }

    //memberEntity -> memberDto
    public static MemberDto toMemberDto(Member member){
        return MemberDto.builder()
                .memId(member.getMemId())
                .nickname(member.getNickname())
                .mannerScore(member.getMannerScore())
                .loc(member.getLoc())
                .build();
    }


}
