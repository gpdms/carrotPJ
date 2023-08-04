package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceImplTest {
    @Autowired
    PostServiceImpl postService;
    @Autowired
    MemberService memberService;
    @Test
    void test() {
        MemberDto tester1 = MemberDto.builder().memId("tester1").loc(Loc.GANGBUK).build();
        List<PostDto> postDtos = postService.selectAllPost(tester1);
    }
}