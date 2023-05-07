package com.exercise.carrotproject.domain.member.service;


import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.BlockDto;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

@Service
public interface MemberService {
    Member findOneMember(String memId);
    MemberDto findOneSocialMemberDto(String email, Role role);
    String getNicknameByMemId(String memId);
    boolean hasDuplicatedMemberId(String memId);
    boolean hasDuplicatedNickname(String nickname);
    boolean hasEmailAndRole(String email, Role role);
    String createMemId();
    Map<String, Object> insertMember(Member member);
    Map<String, Object> insertSocialMember(Map<String, Object> userinfo, Role role);
    String saveSocialProfImg(String url);

    Member findMemberForProfileEdit(String memId);
    String getProfPath(String memId);
    String createProfPath(MultipartFile img);
    void saveImgServer(MultipartFile profImg, String save_path);

    boolean isPwdUpdated(String memId, String newPwd);
    Map<String, Object> profileUpdate(Member updateMember, MultipartFile profImg);
    long temporaryPwdUdpate(String email) throws MessagingException, UnsupportedEncodingException;

    Block findOneBlockByFromMemToMem (String fromMemId, String toMemId);
    boolean existBlockByFromMemToMem (String fromMemId, String toMemId);
    boolean existBlockByMemIds (String memId1, String memId2);
    Map<String,String> insertBlock(String fromMemId, String toMemId);
    void deleteBlock(String fromMemId, String toMemId);
}