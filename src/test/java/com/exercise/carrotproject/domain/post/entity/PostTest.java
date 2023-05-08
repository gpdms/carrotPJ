package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.member.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PostTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    SecurityUtils securityUtils;

    @Test
    @Transactional
    @Rollback(value = false)
    public void postIns(){
        Member member3 = Member.builder().memId("tester3").mannerScore(365000.0).nickname("3Nick").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("tester33")).role(Role.USER).build();
        Member member2 = Member.builder().memId("tester2").mannerScore(365000.0).nickname("2Nick").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("tester22")).role(Role.USER).build();
        Member member1 = Member.builder().memId("tester1").mannerScore(365000.0).nickname("1Nick").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("tester11")).role(Role.USER).build();
        Member admin = Member.builder().memId("admin1").mannerScore(365000.0).nickname("adminNick").loc(Loc.GANGSEO).memPwd(securityUtils.getHashedPwd("admin1234")).role(Role.ADMIN).build();

        for(int i=1; i<=100; i++){
            Post post = Post.builder()
                    .title("제목"+i)
                    .content("내용"+i)
                    .category(Category.BEAUTY)
                    .price(5000)
                    .member(member2)
                    .loc(Loc.GANGBUK)
                    .hits(0)
                    .build();
            postRepository.save(post);
        }
//        Post post = Post.builder()
//                .title("제목")
//                .content("내용")
//                .category(Category.BEAUTY)
//                .build();
//        postRepository.save(post);

    }
}