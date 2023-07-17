package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.JoinNormalMemberDto;
import com.exercise.carrotproject.domain.member.dto.JoinSocialMemberDto;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.dto.ProfileImgInfo;
import com.exercise.carrotproject.domain.member.util.GenerateUtils;
import com.exercise.carrotproject.domain.member.dto.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final EmailServiceImpl emailService;

    @Override
    public Member findMemberByMemId(String memId) {
        return memberRepository.findById(memId)
                .orElseThrow(() -> new NoSuchElementException("Member Not Found"));
    }

    @Override
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Member Not Found"));
    }

    @Override
    public boolean hasMemId(String memId) {
        return memberRepository.existsById(memId);
    }
    @Override
    public boolean hasEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
    @Override
    public boolean hasEmailAndRole(String email, Role role) {
        return memberRepository.existsByEmailAndRole(email, role);
    }

    @Override
    public MemberDto login(String loginId, String loginPwd) {
        Member member = this.findMemberByMemId(loginId);
        return member.isPwdMatch(loginPwd) ? MemberEntityDtoMapper.toDto(member) : null;
    }

    @Override
    public MemberDto login(String email, Role role) {
        Member member = findMemberByEmailAndRole(email, role);
        return MemberEntityDtoMapper.toDto(member);
    }

    private Member findMemberByEmailAndRole(String email, Role role) {
        return memberRepository.findByEmailAndRole(email, role)
                .orElseThrow(() -> new NoSuchElementException("Member Not Found"));
    }

    @Override
    public String issueAuthCodeByEmail(String email) {
        String authCode = GenerateUtils.generateEmailAuthCode();
        emailService.sendAuthCodeByEmail(authCode, email);
        return authCode;
    }

    @Override
    @Transactional
    public void joinNormalMember(JoinNormalMemberDto memberDto) {
        Member member = Member.builder()
                .memId(memberDto.getMemId())
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .loc(memberDto.getLoc())
                .memPwd(memberDto.getMemPwd())
                .role(Role.NORMAL).build();
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void joinSocialMember(JoinSocialMemberDto memberDto) {
        ProfileImgInfo profImgInfo = processProfileImg(memberDto.getProfImgUrl());
        String fullProfPath = profImgInfo == null ? null : profImgInfo.getFullProfPath();
        Member member = Member.builder()
                .memId(GenerateUtils.generateUniqueMemId())
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .loc(memberDto.getLoc())
                .role(memberDto.getRole())
                .profPath(fullProfPath)
                .build();
        memberRepository.save(member);
    }

    private ProfileImgInfo processProfileImg(String imgUrl) {
        if(!hasText(imgUrl)) {
            return null;
        }
        ProfileImgInfo profImgInfo = ProfileImgInfo.of(imgUrl);
        profImgInfo.mkProfImgDir();
        saveImgToServer(imgUrl, profImgInfo.getFullProfPath());
        return profImgInfo;
    }

    private void saveImgToServer(String imgUrl, String savePath) {
        try {
            URL imgURL = new URL(imgUrl);
            BufferedImage image = ImageIO.read(imgURL);
            String extension = imgUrl.substring(imgUrl.lastIndexOf(".")+1);
            File file = new File(savePath);
            ImageIO.write(image, extension, file);
        } catch (IOException e) {
            log.error("MemberService saveImgToServer() : " + e);
        }
    }

    @Override
    public boolean isEmptyOrImageFile(MultipartFile profImg) {
        return profImg.isEmpty() || profImg.getContentType().startsWith("image");
    }

    @Override
    @Transactional
    public void changeProfile(MemberDto memberDto, MultipartFile profImg) {
        ProfileImgInfo profImgInfo = processProfileImg(profImg);
        String fullProfPath = profImgInfo == null ? null : profImgInfo.getFullProfPath();
        Member member = this.findMemberByMemId(memberDto.getMemId());
        Member updateMember = Member.builder().memId(memberDto.getMemId())
                .nickname(memberDto.getNickname())
                .loc(memberDto.getLoc())
                .profPath(fullProfPath).build();
        member.updateProfile(updateMember);
    }

    private ProfileImgInfo processProfileImg(MultipartFile file) {
        if(file.isEmpty()) {
            return null;
        }
        ProfileImgInfo profImgInfo = ProfileImgInfo.of(file);
        profImgInfo.mkProfImgDir();
        saveImgToServer(file, profImgInfo.getFullProfPath());
        return profImgInfo;
    }

    private void saveImgToServer(MultipartFile profImg, String savePath){
        File file = new File(savePath);
        try {
            profImg.transferTo(file);
        } catch (IOException e) {
            log.error("MemberService saveImgToServer() : " + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteProfImg(String memId) {
        Member member = this.findMemberByMemId(memId);
        String oldSavePath = member.getProfPath();
        if (!hasText(oldSavePath)) {
            return;
        }
        member.resetProfPath();
        deleteServerFile(oldSavePath);
    }

    private void deleteServerFile(String savePath) {
        Path filePath = Paths.get(savePath);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) { //파일이 열려있거나, JVM 또는 다른 응용프로그램에서 사용중일 때
            log.error("MemberService deleteFile(): {} ", e);
        }
    }

    @Override
    @Transactional
    public void changePwdByMemId(String newPwd, String memId) {
        Member member = this.findMemberByMemId(memId);
        if (member.isPwdMatch(newPwd)) {
            return;
        }
        member.updateMemPwd(newPwd);
    }

    @Override
    @Transactional
    public void resetAndSendTemporaryPwdByEmail(String email) {
        String tempPwd = GenerateUtils.generateTempPwd();
        Member member = findMemberByEmail(email);
        member.updateMemPwd(tempPwd);
        emailService.sendTemporaryPwdByEmail(tempPwd, email);
    }
}
