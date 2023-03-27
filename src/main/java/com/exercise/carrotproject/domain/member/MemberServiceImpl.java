package com.exercise.carrotproject.domain.member;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Value("${dir.img-profile}")
    private String rootProfileImgDir;

    @Override
    public boolean checkDuplicatedMember(String memId) {
        return memberRepository.findById(memId).isEmpty() ? false : true;
    }

    @Override
    public Map<String, Object> saveMember(Member member) {
        Map<String, Object> saveResult = new HashMap<>();
        if (checkDuplicatedMember(member.getMemId())) {
            saveResult.put("resultCode", "fail-DM");
            return saveResult;
        }

        Member newMember = memberRepository.save(member);
        saveResult.put("resultCode", "success");
        return saveResult;
    }

    @Override
    public Member findMemberForProfileEdit(String memId) {
        Member member = memberRepository.findById(memId).orElse(null);
        return Member.builder().profPath(member.getProfPath())
                .nickname(member.getNickname())
                .loc(member.getLoc()).build();
    }

    @Override
    public String getProfPath(String memId) {
        return memberRepository.findById(memId).orElseThrow().getProfPath();
    }

    @Override
    @Transactional
    public boolean isPwdUpdated(String memId, String newPwd) {
        Member member = memberRepository.findById(memId).orElseThrow(
                ()-> new NoSuchElementException());
        member.updatePwd(newPwd);

        return member.getMemPwd().equals(newPwd) ? true : false;
    }

    //프로필 이미지 경로 생성
    public String createProfPath(MultipartFile img) {
        if (img == null) {
            return null;
        }
        if (img.getContentType().startsWith("image") == false) {
            log.warn("this file is not image type");
            return null;
        }

        //디렉토리 생성
        File profImgDir = new File(rootProfileImgDir + LocalDate.now());
        profImgDir.mkdir();

        //이미지 path 생성
        String origin_name = img.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = origin_name.substring(origin_name.lastIndexOf("."));
        String save_name = uuid + extension;
        String save_path = profImgDir + "/" + save_name;
        return save_path;
    }

    public void saveImgServer(MultipartFile profImg, String save_path){
        try {
            profImg.transferTo(new File(save_path));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public Member profileUpdate(Member updateMember, MultipartFile profImg) {
        String profPath = createProfPath(profImg);
        Member member = memberRepository.findById(updateMember.getMemId()).orElseThrow(
                ()-> new NoSuchElementException());
        if(profPath != null) {
            saveImgServer(profImg, profPath);
            member.updateProfile(updateMember.getNickname(), profPath, updateMember.getLoc());
            log.info("afterUpdateMember {}",member);
            return member;
        }
        return member;
    }
}
