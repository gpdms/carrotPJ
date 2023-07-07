package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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
