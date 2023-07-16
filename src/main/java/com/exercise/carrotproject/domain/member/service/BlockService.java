package com.exercise.carrotproject.domain.member.service;


import com.exercise.carrotproject.domain.member.dto.MyBlockDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface BlockService {
    Block findBlockByFromMemToMem (String fromMemId, String toMemId);
    boolean hasBlockByFromMemToMem (String fromMemId, String toMemId);
    boolean hasBlockByMemIds (String memId1, String memId2);

    void insertBlock(String fromMemId, String toMemId);
    void deleteBlock(String fromMemId, String toMemId);
    List<MyBlockDto> getMyBlocks(String fromMemId);
}