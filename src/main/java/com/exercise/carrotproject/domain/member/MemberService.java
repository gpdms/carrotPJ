package com.exercise.carrotproject.domain.member;


import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.MemberDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public interface MemberService {
    public boolean checkDuplicatedMember(String memId);
    public Map<String,Object> saveMember(Member member);

    public Member findMemberForProfileEdit(String memId);
    public String getProfPath(String memId);
    public boolean isPwdUpdated(String memId, String newPwd);
    public Member profileUpdate(Member member, MultipartFile profImg);

}
