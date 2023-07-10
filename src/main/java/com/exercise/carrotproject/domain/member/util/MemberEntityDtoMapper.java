package com.exercise.carrotproject.domain.member.util;

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
                .email(member.getEmail())
                .nickname(member.getNickname())
                .mannerScore(
                        roundedMannerScore >= 99.9 ? 99.9 : roundedMannerScore)
                .loc(member.getLoc())
                .role(member.getRole())

                .build();
    }

    public static Member toSignupSocialEntity(MemberDto memberDto) {
        return Member.builder()
                .memId(memberDto.getMemId())
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .loc(memberDto.getLoc())
                .profPath(memberDto.getProfPath())
                .role(memberDto.getRole())
                .build();
    }

    public static Member toSignupNomalEntity(MemberDto memberDto) {
        return Member.builder()
                .memId(memberDto.getMemId())
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .loc(memberDto.getLoc())
                .memPwd(memberDto.getMemPwd())
                .role(Role.NORMAL).build();
    }
}
