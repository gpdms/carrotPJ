package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.member.dto.MyBlockDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new NoSuchElementException("Block Not Found : Block by FromMem and ToMem"));
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
    public void insertBlock(String fromMemId, String toMemId) {
        Member fromMem = memberService.findMemberByMemId(fromMemId);
        Member toMem = memberService.findMemberByMemId(toMemId);
        Block newBlock = Block.builder()
                .fromMem(fromMem)
                .toMem(toMem).build();
        blockRepository.save(newBlock);
    }

    @Override
    @Transactional
    public void deleteBlock(String fromMemId, String toMemId) {
        Block block = findBlockByFromMemToMem(fromMemId, toMemId);
        blockRepository.deleteById(block.getBlockId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyBlockDto> getMyBlocks(String fromMemId) {
        Member fromMem = memberService.findMemberByMemId(fromMemId);
        return fromMem.getBlockFromMemList()
                .stream()
                .map(block -> MyBlockDto.of(block))
                .collect(Collectors.toList());
    }
}
