package com.exercise.carrotproject.domain.member.service;


import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MemberService {
    Member findMemberByMemId(String memId);
    Member findMemberByEmail(String email);
    boolean hasMemId(String memId);
    boolean hasEmail(String email);
    boolean hasEmailAndRole(String email, Role role);

    MemberDto login(String loginId, String loginPwd);
    MemberDto login(String email, Role role);

    void insertNormalMember(MemberDto member);
    void insertSocialMember(MemberDto member);

    String issueAuthCodeByEmail(String email);
    void resetAndSendTemporaryPwdByEmail(String email);

    boolean isImageFile(MultipartFile profImg);
    void changeProfile(MemberDto updateMember, MultipartFile profImg);
    void changePwdByMemId(String newPwd, String memId);
    void deleteProfImg(String memId);
}