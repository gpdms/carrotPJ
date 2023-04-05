package com.exercise.carrotproject.domain.chat.service;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRoomRepository;
import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class ChatServiceImplTest {
    @Autowired
    ChatService chatService;

    @PersistenceContext
    EntityManager em;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void saveMemberAndPostTest() {
        Member member = Member.builder()
                .memId("tester2")
                .memPwd("12345678")
                .nickname("테스터2")
                .loc(Loc.GANGDONG)
                .build();
        em.persist(member);

        Post post = Post.builder()
                .title("테스트글2")
                .member(member)
                .price(2000)
                .content("테스트내용2")
                .loc(member.getLoc())
                .category(Category.BEAUTY)
                .build();
        em.persist(post);
    }

   /* @Test
    @Transactional
    @Rollback(value = false)
    public void saveChatTest(){
        Member member = em.find(Member.class, "tester");
        Post post = em.find(Post.class, 1L);
        Chat chat = Chat.builder()
                .post(post)
                .from(member)
                .message("테스트메세지입니다.")
                .build();
        chatService.saveChat(chat);
    }*/
}