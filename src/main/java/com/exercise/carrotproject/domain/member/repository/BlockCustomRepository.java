package com.exercise.carrotproject.domain.member.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface BlockCustomRepository  {
    boolean existsBlockByFromMemToMem(String fromMemId, String toMemId);
    boolean existsBlockByMemIds(String memId1, String memId2);
}
