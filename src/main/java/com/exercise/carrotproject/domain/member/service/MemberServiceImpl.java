package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.util.GenerateUtils;
import com.exercise.carrotproject.domain.member.util.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final EmailServiceImpl emailService;

    @Value("${file.postImg}")
    private String rootImgDir;

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
        Member member = findMemberByMemId(loginId);
        return member.isPwdMatch(loginPwd) ? MemberEntityDtoMapper.toDto(member) : null;
    }

    @Override
    public MemberDto login(String email, Role role) {
        Member member = findMemberByEmailAndRole(email, role);
        return MemberEntityDtoMapper.toDto(member);
    }
    private Member findMemberByEmailAndRole(String email, Role role) {
        return memberRepository.findByEmailAndRole(email, role)
                .orElseThrow(() -> new NoSuchElementException(role.name() + "Member Not Found"));
    }

    @Override
    @Transactional
    public String issueAuthCodeByEmail(String email) {
        String authCode = GenerateUtils.generateEmailAuthCode();
        emailService.sendAuthCodeByEmail(authCode, email);
        return authCode;
    }

    @Override
    @Transactional
    public void insertNormalMember(MemberDto memberDto) {
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
    public void insertSocialMember(MemberDto memberDto) {
        String imgUrl = memberDto.getProfPath();
        String savePath = "";
        if(!hasText(imgUrl)) {
            savePath = createProfPath(imgUrl);
            saveImgToServer(imgUrl, savePath);
        }
        Member member = Member.builder()
                .memId(GenerateUtils.generateUniqueMemId())
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .loc(memberDto.getLoc())
                .role(memberDto.getRole())
                .profPath(savePath)
                .build();
        memberRepository.save(member);
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
            throw new RuntimeException(e);
        }
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

    private String createProfPath(String url) {
        String profDirPath = makeProfDir();
        String extension = url.substring(url.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String saveName = uuid + extension;
        String savePath = profDirPath + File.separator + saveName;
        return savePath;
    }
    private String createProfPath(MultipartFile profImg) {
        String profDirPath = makeProfDir();
        String originName = profImg.getOriginalFilename();
        String extension = originName.substring(originName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String saveName = uuid + extension;
        String savePath = profDirPath + File.separator + saveName;
        return savePath;
    }

    private String makeProfDir() {
        String profDirPath = rootImgDir + File.separator + "member" + File.separator + LocalDate.now();
        new File(profDirPath).mkdir();
        return profDirPath;
    }

    @Override
    public boolean isImageFile(MultipartFile profImg) {
        return  profImg.isEmpty() || profImg.getContentType().startsWith("image");
    }

    @Override
    @Transactional
    public void changeProfile(MemberDto memberDto, MultipartFile profImg) {
        Member member = findMemberByMemId(memberDto.getMemId());
        String profPath = member.getProfPath();
        if(!profImg.isEmpty()) {
            profPath = createProfPath(profImg);
            saveImgToServer(profImg, profPath);
        }
        Member updateMember = Member.builder().memId(memberDto.getMemId())
                .nickname(memberDto.getNickname())
                .loc(memberDto.getLoc())
                .profPath(profPath).build();
        member.updateProfile(updateMember);
    }

    @Override
    @Transactional
    public void deleteProfImg(String memId) {
        Member member = findMemberByMemId(memId);
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
        Member member = findMemberByMemId(memId);
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
