package com.exercise.carrotproject.domain.member;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public boolean foundDuplicatedMember(String memId) {
        return memberRepository.findById(memId).isEmpty() ? false : true;
    }
    @Override
    public Map<String, Object> saveMember(Member member) {
        Map<String, Object> saveResult =  new HashMap<>();
        if(foundDuplicatedMember(member.getMemId())) {
           saveResult.put("resultCode", "fail-DM");
           return saveResult;
        }

        Member newMember = memberRepository.save(member);
        saveResult.put("resultCode", "success");
        return saveResult;
    }

    @Override
    public MemberDto findMemberForProfileEdit(String memId) {
        Member member = memberRepository.findById(memId).orElse(null);
        return  MemberDto.builder().profPath(member.getProfPath())
                .nickname(member.getNickname())
                .loc(member.getLoc()).build();
    }


}
