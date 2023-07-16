package com.exercise.carrotproject.domain.member.dto;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;

public class MemberEntityDtoMapper {
    //memberDto -> memberEntity
    public static Member toEntity(MemberDto memberDto){
        return Member.builder()
                .memId(memberDto.getMemId())
                .nickname(memberDto.getNickname())
                .loc(memberDto.getLoc())
                .role(memberDto.getRole())
                .build();
    }

    //memberEntity -> memberDto
    public static MemberDto toDto(Member member){
        double roundedMannerScore = Math.round(member.getMannerScore() / 10000.0 * 10) / 10.0;
        return MemberDto.builder()
                .memId(member.getMemId())
                .nickname(member.getNickname())
                .mannerScore(
                        roundedMannerScore >= 99.9 ? 99.9 : roundedMannerScore)
                .loc(member.getLoc())
                .role(member.getRole())
                .build();
    }
}
