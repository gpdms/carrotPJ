package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
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
public class MemberServiceImpl {
    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;
    private final SecurityUtils securityUtils;

    @Value("${dir.img-profile}")
    private String rootProfileImgDir;

    //소셜로그인을 위한 아이디 난수생성
    public String createMemId() {
        return UUID.randomUUID().toString();
    }

    //@Override
    public Member findOneMember(String memId) {
        return memberRepository.findById(memId)
               .orElseThrow(() -> new NoSuchElementException("Member Not Found"));
    }

    //@Override
    public boolean hasDuplicatedMemberId(String memId) {
        return memberRepository.existsById(memId) ? true : false;
    }
    public boolean hasDuplicatedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname) ? true : false;
    }

    //@Override
    public Map<String, Object> insertMember(Member member) {
        Map<String, Object> saveResult = new HashMap<>();
        if (hasDuplicatedMemberId(member.getMemId())) {
            saveResult.put("fail", "id");
            return saveResult;
        }
        if(hasDuplicatedNickname(member.getNickname())) {
            saveResult.put("fail", "nickname");
            return saveResult;
        }
        Member newMember = memberRepository.save(member);
        saveResult.put("success", "저장 성공");
        return saveResult;
    }


    @Transactional
    public Map<String, Object> insertSocialMember(Map<String, Object> userinfo, Role role) {
        HashMap<String, Object> resultMap = new HashMap<>();
        if(hasDuplicatedNickname(userinfo.get("nickname").toString())) {
            resultMap.put("fail", "nickname");
            return resultMap;
        }
        String save_path = userinfo.get("profPath").toString();
        if(!save_path.isEmpty()) {
            save_path = saveSocialProfImg(save_path);
        }
        Member member = Member.builder().memId(createMemId())
                .mannerScore(36.5)
                .email(userinfo.get("email").toString())
                .nickname(userinfo.get("nickname").toString())
                .loc((Loc)userinfo.get("loc"))
                .profPath(save_path)
                .role(role)
                .build();
        memberRepository.save(member);
        resultMap.put("success", MemberEntityDtoMapper.toMemberDto(member));
        return resultMap;
    }

    public String saveSocialProfImg(String url) {
        String save_path = url;
        try {
            URL imgURL = new URL(url);
            String extension = url.substring(url.lastIndexOf(".")+1); // 확장자

            File profImgDir = new File(rootProfileImgDir + LocalDate.now());
            profImgDir.mkdir();

            String uuid = UUID.randomUUID().toString();
            String save_name = uuid +"."+ extension;
            save_path = profImgDir + "/" + save_name;

            BufferedImage image = ImageIO.read(imgURL);
            File file = new File(save_path);

            ImageIO.write(image, extension, file); // image를 file로 업로드
            System.out.println("이미지 업로드 완료!");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return save_path;
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
        if (profImg.getContentType().startsWith("image") == false) {
            return null;
        }
        Member member = memberRepository.findById(updateMember.getMemId()).orElseThrow(
                ()-> new NoSuchElementException());
        String profPath = member.getProfPath();
        if(profPath != null) {
            profPath = createProfPath(profImg);
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
    public Map<String,String> insertBlock(String fromMemId, String toMemId) {
        Block block = Block.builder().fromMem(memberRepository.findById(fromMemId).orElse(null))
                .toMem(memberRepository.findById(toMemId).orElse(null)).build();
        Map<String, String> saveResult = new HashMap<>();
        if (blockRepository.save(block) != null) {
            saveResult.put("resultCode", "fail");
        }
        saveResult.put("resultCode", "success");
        return saveResult;
    }

    //@Override
    public void deleteBlock(String fromMemId, String toMemId) {
        Block block = findOneBlockByMemIds(fromMemId, toMemId);
        blockRepository.deleteById(block.getBlockId());
    }

    public boolean hasSocialMember(String email, Role role) {
        return memberRepository.existsByEmailAndRole(email, role);
    }
    public MemberDto findOneSocialMemberDto(String email, Role role) {
        Member member = memberRepository.findByEmailAndRole(email, role);
        return  MemberEntityDtoMapper.toMemberDto(member);
    }


}
