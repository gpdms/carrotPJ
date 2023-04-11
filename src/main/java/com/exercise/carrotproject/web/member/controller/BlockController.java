package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.web.common.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BlockController {
    private final MemberServiceImpl memberService;
    @PostMapping("/block/{memId}")
    public ResponseEntity<Map<String, String>> blockMember(@PathVariable String memId,
                                                           HttpSession session){
        MemberDto loginMember = (MemberDto)session.getAttribute(SessionConst.LOGIN_MEMBER);
        Map<String, String> resultMap = memberService.insertBlock(loginMember.getMemId(), memId);
        if(resultMap.containsValue("fail")) {
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
    @DeleteMapping("/block/{memId}")
    public ResponseEntity<Map<String, String>> cancelBlockMember(@PathVariable String memId,
                                    HttpSession session){
        MemberDto loginMember = (MemberDto)session.getAttribute(SessionConst.LOGIN_MEMBER);
        memberService.deleteBlock(loginMember.getMemId(), memId);
        return new ResponseEntity<>(Collections.singletonMap("message", "취소에 성공했습니다."), HttpStatus.OK);
    }
}
