package com.exercise.carrotproject.domain.member.repository;

import com.exercise.carrotproject.domain.member.dto.MyBlockDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockCustomRepository  {
    boolean existsBlockByFromMemToMem(String fromMemId, String toMemId);
    boolean existsBlockByMemIds(String memId1, String memId2);
    List<MyBlockDto> findBlocksByFromMemId(String fromMemId);
}
