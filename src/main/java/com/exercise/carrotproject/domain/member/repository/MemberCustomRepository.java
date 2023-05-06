package com.exercise.carrotproject.domain.member.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberCustomRepository {
    String selectNicknameByMemId(String memId);
    boolean hasBlockByMemIds(String memId1, String memId2);
    long updateTemporaryPwd(String email, String hashedPwd);
}
