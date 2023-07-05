package com.exercise.carrotproject.domain.member.service;


import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public interface MemberService {
    Member findMemberByMemId(String memId);
    Member findMemberByEmail(String email);
    Member findMemberByEmailAndRole(String email, Role role);
    boolean hasDuplicatedMemId(String memId);
    boolean hasDuplicatedEmail(String email);
    boolean hasDuplicatedEmailAndRole(String email, Role role);

    Member login(String loginId, String loginPwd);
    void insertMember(Member member);
    Map<String, Object> insertSocialMember(MemberDto memberDto);

    String sendAuthCodeByEmail(String email);
    void issueTemporaryPwdByEmail(String email);

    Map<String, Object> changeProfile(Member updateMember, MultipartFile profImg);
    void changePwdByMemId(String newPwd, String memId);
}