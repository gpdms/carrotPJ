package com.exercise.carrotproject.domain.member;


import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.MemberDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface MemberService {
    public boolean foundDuplicatedMember(String memId);
    public Map<String,Object> saveMember(Member member);

    public MemberDto findMemberForProfileEdit(String memId);
}
