package com.exercise.carrotproject.domain.member;


import com.exercise.carrotproject.domain.member.dto.BlockDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
public interface MemberService {
    public Optional<Member> findOneMember(String memId);
    public boolean checkDuplicatedMember(String memId);
    public Map<String,Object> insertMember(Member member);

    public Member findMemberForProfileEdit(String memId);
    public String getProfPath(String memId);
    public boolean isPwdUpdated(String memId, String newPwd);
    public Member profileUpdate(Member member, MultipartFile profImg);

    public Block findOneBlockByMemIds(String fromMemId, String toMemId);
    public void insertBlock(Block block);
    public void deleteBlock(String fromMemId, String toMemId);

}
