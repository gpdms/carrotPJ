package com.exercise.carrotproject.domain.member.service;


import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public interface BlockService {
    Block findBlockByFromMemToMem (String fromMemId, String toMemId);
    boolean hasBlockByFromMemToMem (String fromMemId, String toMemId);
    boolean hasBlockByMemIds (String memId1, String memId2);
    Map<String, Object> insertBlock(String fromMemId, String toMemId);
    void deleteBlock(String fromMemId, String toMemId);
}