package com.exercise.carrotproject.domain.member;


import com.exercise.carrotproject.domain.member.dto.BlockDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

//@Service
public interface MemberService {
    Optional<Member> findOneMember(String memId);
    boolean hasDuplicatedMemberId(String memId);
    Map<String,Object> insertMember(Member member);

    Member findMemberForProfileEdit(String memId);
    String getProfPath(String memId);
    boolean isPwdUpdated(String memId, String newPwd);
    Member profileUpdate(Member member, MultipartFile profImg);

    Block findOneBlockByMemIds(String fromMemId, String toMemId);
    void insertBlock(String fromMemId, String toMemId);
    void deleteBlock(String fromMemId, String toMemId);

}