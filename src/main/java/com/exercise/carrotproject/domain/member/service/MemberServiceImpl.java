package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.BlockRepository;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.web.member.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl {
    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;
    private final SecurityUtils securityUtils;

    @Value("${dir.img-profile}")
    private String rootProfileImgDir;


    //@Override
    public Optional<Member> findOneMember(String memId) {
       return memberRepository.findById(memId);
    }

    //@Override
    public boolean hasDuplicatedMemberId(String memId) {
        return memberRepository.existsById(memId) ? true : false;
    }

    //@Override
    public Map<String, Object> insertMember(Member member) {
        Map<String, Object> saveResult = new HashMap<>();
        if (hasDuplicatedMemberId(member.getMemId())) {
            saveResult.put("resultCode", "fail-DM");
            return saveResult;
        }
        Member newMember = memberRepository.save(member);
        saveResult.put("resultCode", "success");
        return saveResult;

    }

    //@Override
    public Member findMemberForProfileEdit(String memId) {
        Member member = memberRepository.findById(memId).orElse(null);
        return Member.builder().profPath(member.getProfPath())
                .nickname(member.getNickname())
                .loc(member.getLoc()).build();
    }

   // @Override
    public String getProfPath(String memId) {
        return memberRepository.findById(memId).orElseThrow().getProfPath();
    }

    //@Override
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

    //@Override
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

    //@Override
    public Block findOneBlockByMemIds(String fromMemId, String toMemId){
        Member fromMem = memberRepository.findById(fromMemId).orElse(null);
        Member toMem = memberRepository.findById(toMemId).orElse(null);
        if(fromMem!=null && toMem!=null) {
          return blockRepository.findByFromMemAndToMem(fromMem, toMem).orElse(null);
        }
        return null;
    }



    //@Override
    public void insertBlock(String fromMemId, String toMemId) {
        Block block = Block.builder().fromMem(memberRepository.findById(fromMemId).orElse(null))
                .toMem(memberRepository.findById(toMemId).orElse(null)).build();
        blockRepository.save(block);
    }

    //@Override
    public void deleteBlock(String fromMemId, String toMemId) {
        Block block = findOneBlockByMemIds(fromMemId, toMemId);
        blockRepository.deleteById(block.getBlockId());
    }



}
