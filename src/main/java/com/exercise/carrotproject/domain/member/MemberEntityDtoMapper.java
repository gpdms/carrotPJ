package com.exercise.carrotproject.domain.member;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;

public class MemberEntityDtoMapper {
    //memberDto -> memberEntity
    public static Member toMemberEntity(MemberDto memberDto){
        return Member.builder()
                .memId(memberDto.getMemId())
                .nickname(memberDto.getNickname())
                .loc(memberDto.getLoc())
                .role(memberDto.getRole())
                .build();
    }
    //memberEntity -> memberDto
    public static MemberDto toMemberDto(Member member){
        return MemberDto.builder()
                .memId(member.getMemId())
                .nickname(member.getNickname())
                .mannerScore(
                        Math.round(member.getMannerScore() / 10000.0 * 10) / 10.0)
                .loc(member.getLoc())
                .role(member.getRole())
                .build();
    }


}
