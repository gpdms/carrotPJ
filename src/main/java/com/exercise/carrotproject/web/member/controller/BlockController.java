package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.service.BlockService;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BlockController {
    private final BlockService blockService;

    @PostMapping("/block/{memId}")
    public ResponseEntity<Map<String, Object>> blockMember(@PathVariable String memId,
                                                           @Login MemberDto loginMember){
        Map<String, Object> resultMap = blockService.insertBlock(loginMember.getMemId(), memId);
        if(resultMap.containsValue("fail")) {
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @DeleteMapping("/block/{memId}")
    public ResponseEntity<Map<String, String>> cancelBlockMember(@PathVariable String memId,
                                                                 @Login MemberDto loginMember){
        blockService.deleteBlock(loginMember.getMemId(), memId);
        return new ResponseEntity<>(Collections.singletonMap("message", "취소에 성공했습니다."), HttpStatus.OK);
    }
}
