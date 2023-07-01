package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.BlockRepository;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService{
    private final MemberService memberService;
    private final BlockRepository blockRepository;

    @Override
    public Block findBlockByFromMemToMem (String fromMemId, String toMemId){
        Member fromMem = memberService.findMemberByMemId(fromMemId);
        Member toMem = memberService.findMemberByMemId(toMemId);
        return blockRepository
                .findByFromMemAndToMem(fromMem, toMem)
                .orElseThrow(() -> new NoSuchElementException("Block Not Found"));
    }

    @Override
    public boolean hasBlockByFromMemToMem (String fromMemId, String toMemId) {
        return blockRepository.existsBlockByFromMemToMem(fromMemId, toMemId);
    }
    @Override
    public boolean hasBlockByMemIds (String memId1, String memId2) {
        return blockRepository.existsBlockByMemIds(memId1, memId2);
    }

    @Override
    @Transactional
    public Map<String, Object> insertBlock(String fromMemId, String toMemId) {
        Map<String, Object> saveResult = new HashMap<>();
        Block newBlock = Block.builder()
                .fromMem(memberService.findMemberByMemId(fromMemId))
                .toMem(memberService.findMemberByMemId(toMemId)).build();
        Block block = blockRepository.save(newBlock);
        if (block == null) {
            saveResult.put("fail", "block");
        }
        saveResult.put("success", block);
        return saveResult;
    }

    @Override
    @Transactional
    public void deleteBlock(String fromMemId, String toMemId) {
        Block block = findBlockByFromMemToMem(fromMemId, toMemId);
        blockRepository.deleteById(block.getBlockId());
    }
}
