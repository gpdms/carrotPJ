package com.exercise.carrotproject.domain.chat.service;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRepository;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRoomRepository;
import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class ChatServiceImplTest {
    @Autowired
    ChatService chatService;

    @PersistenceContext
    EntityManager em;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatRepository chatRepository;

    //저장테스트
//    @Test
//    @Transactional
//    @Rollback(value = false)
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

    //채팅 날짜별 분류 테스트
//    @Test
    void makeSection() {
        Map<String, List<Chat>> chatSectionList = new HashMap<>();
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(1L);
        List<Chat> chatList = chatRepository.findByRoom(chatRoom.get());
        chatList.stream().forEach(chat->{
            Timestamp createdTime = chat.getCreatedTime();
            long time = createdTime.getTime();
            String format = new SimpleDateFormat("yyyy-MM-dd").format(time);
            if (chatSectionList.get(format) == null) {
                chatSectionList.put(format, new ArrayList<Chat>());
                chatSectionList.get(format).add(chat);
            } else {
                chatSectionList.get(format).add(chat);
            }
        });
        for (String key : chatSectionList.keySet()) {
            System.out.println("key >>> " + key);
            for (Chat chat : chatSectionList.get(key)) {
                System.out.println("chat >>> " + chat);
            }
            System.out.println("-----------------------------------------");
        }
    }

    //채팅 저장 테스트
//    @Test
//    @Transactional
//    @Rollback(value = false)
    public void saveChatTest(){
        Member member = em.find(Member.class, "tester");
        Post post = em.find(Post.class, 1L);
        Chat chat = Chat.builder()
                .post(post)
                .from(member)
                .message("테스트메세지입니다.")
                .build();
//        chatService.saveChat(chat);
    }
}