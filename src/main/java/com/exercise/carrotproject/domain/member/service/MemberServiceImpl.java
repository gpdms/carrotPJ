package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.util.GenerateUtils;
import com.exercise.carrotproject.domain.member.util.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

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
    public Member findMemberByEmailAndRole(String email, Role role) {
        return memberRepository.findByEmailAndRole(email, role)
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
    public Member login(String loginId, String loginPwd) {
        Member member = findMemberByMemId(loginId);
        boolean pwdMatch = member.isPwdMatch(loginPwd);
        return member.isPwdMatch(loginPwd) ? member : null;
    }

    @Override
    @Transactional
    public String sendAuthCodeByEmail(String email) {
        String authCode = GenerateUtils.generateEmailAuthCode();
        emailService.sendAuthCodeByEmail(authCode, email);
        return authCode;
    }

    @Override
    @Transactional
    public void insertMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public Map<String, Object> insertSocialMember(MemberDto memberDto) {
        HashMap<String, Object> resultMap = new HashMap<>();
        String url = memberDto.getProfPath();
        if(!url.isEmpty()) {
            String savePath = saveUrlImgToServer(url);
            memberDto.setProfPath(savePath);
        }
        memberDto.setMemId(GenerateUtils.generateUniqueMemId());
        memberDto.setRole(Role.SOCIAL_KAKAO);
        Member member = MemberEntityDtoMapper.toSocialMemberEntity(memberDto);
        memberRepository.save(member);
        resultMap.put("success", MemberEntityDtoMapper.toMemberDto(member));
        return resultMap;
    }


    private String saveUrlImgToServer(String url) {
        String savePath;
        try {
            URL imgURL = new URL(url);
            BufferedImage image = ImageIO.read(imgURL);
            String extension = url.substring(url.lastIndexOf(".")+1);
            savePath = createProfPath(url);
            File file = new File(savePath);
            ImageIO.write(image, extension, file);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return savePath;
    }
    private void saveImgToServer(MultipartFile profImg){
        String savePath = createProfPath(profImg);
        try {
            profImg.transferTo(new File(savePath));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }
    private String createProfPath(MultipartFile profImg) {
        String profDirPath = createProfDir();
        String originName = profImg.getOriginalFilename();
        String extension = originName.substring(originName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String saveName = uuid + extension;
        String savePath = profDirPath + File.separator + saveName;
        return savePath;
    }
    private String createProfPath(String url) {
        String profDirPath = createProfDir();
        String extension = url.substring(url.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String saveName = uuid + extension;
        String savePath = profDirPath + File.separator + saveName;
        return savePath;
    }
    private String createProfDir(){
        String profDirPath = rootImgDir + File.separator + "member" + File.separator + LocalDate.now();
        new File(profDirPath).mkdir();
        return profDirPath;
    }

    @Override
    @Transactional
    public void changePwdByMemId (String newPwd, String memId) {
        Member member = findMemberByMemId(memId);
        if (member.isPwdMatch(newPwd)) {
            return;
        }
        member.updateMemPwd(newPwd);
    }

    @Override
    @Transactional
    public Map<String, Object> changeProfile(Member updateMember, MultipartFile profImg) {
        Map<String, Object> updateResult = new HashMap<>();

        Member member = findMemberByMemId(updateMember.getMemId());
        String newProfPath = member.getProfPath();
        //프로필 이미지를 변경한다면,
        if(!profImg.isEmpty()) {
            boolean isImageType = profImg.getContentType().startsWith("image");
            if(!isImageType) {
                updateResult.put("fail", "image");
                return updateResult;
            } else {
                newProfPath = createProfPath(profImg);
                saveImgToServer(profImg) ;
            }
        }

        member.updateProfile(updateMember);
        updateResult.put("success", member);
        return updateResult;
    }

    @Override
    @Transactional
    public void issueTemporaryPwdByEmail (String email) {
        String tempPwd = GenerateUtils.generateTempPwd();
        Member member = findMemberByEmail(email);
        member.updateMemPwd(tempPwd);
        emailService.sendTemporaryPwdByEmail(tempPwd, email);
    }
}
