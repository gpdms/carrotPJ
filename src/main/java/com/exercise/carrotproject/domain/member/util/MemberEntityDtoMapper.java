package com.exercise.carrotproject.domain.member.util;

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
        double roundedMannerScore = Math.round(member.getMannerScore() / 10000.0 * 10) / 10.0;
        return MemberDto.builder()
                .memId(member.getMemId())
                .nickname(member.getNickname())
                .mannerScore(
                        roundedMannerScore >= 99.9 ? 99.9 : roundedMannerScore)
                .loc(member.getLoc())
                .role(member.getRole())
                .email(member.getEmail())
                .build();
    }

    public static Member toSocialMemberEntity(MemberDto memberDto) {
        return Member.builder()
                .memId(memberDto.getMemId())
                .email(memberDto.getEmail())
                .profPath(memberDto.getProfPath())
                .loc(memberDto.getLoc())
                .nickname(memberDto.getNickname())
                .role(memberDto.getRole())
                .build();
    }
}
