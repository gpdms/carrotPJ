package com.exercise.carrotproject.domain;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.chat.entity.QChat;
import com.exercise.carrotproject.domain.chat.entity.QChatRoom;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.QMember;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@SpringBootTest
public class QueryDslTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    EntityManager em;

/*    @Test
    public void test1() {
        QMember member = QMember.member;
        jpaQueryFactory = new JPAQueryFactory(em);
        List<Member> list = jpaQueryFactory.selectFrom(member).fetch();
//        List<Tuple> tuples = jpaQueryFactory.select(emp.ename, emp.job, emp.sal)
//                .from(emp).fetch();
        list.stream().forEach(t->{
            System.out.println("t = " + t);
        });
    }*/

    @Test
    public void test2(){
//        Member build = Member.builder().memPwd("jk652222").memId("jk65222").loc(Loc.GANGBUK).nickname("22").build();
//        em.persist(build);
//        Member member = em.find(Member.class, build.getMemId());
//        System.out.println("member = " + member);

    }

    @Test
    public void getChatRoomAndLastChatTest(){
        List<Tuple> list = jpaQueryFactory.select(QChatRoom.chatRoom, QChat.chat.chatId.max())
                .from(QChatRoom.chatRoom)
                .leftJoin(QChat.chat).on(QChatRoom.chatRoom.roomId.eq(QChat.chat.room.roomId))
                .where(QChatRoom.chatRoom.seller.memId.eq("admin1").or(QChatRoom.chatRoom.buyer.memId.eq("admin1")))
                .fetch();
        System.out.println("------------조회 끝----------");
        list.stream().forEach(t->{
            System.out.println("--0--");
            System.out.println(t.get(QChatRoom.chatRoom));
            System.out.println(t.get(QChatRoom.chatRoom).getRoomId());
            System.out.println(t.get(QChatRoom.chatRoom).getPost());
            System.out.println(t.get(QChatRoom.chatRoom).getSeller());
            System.out.println(t.get(QChatRoom.chatRoom).getBuyer());

//            System.out.println("--1--");
//            System.out.println(t.get(QChatRoom.chatRoom.roomId));
//            System.out.println("--2--");
//            System.out.println(t.get(QChatRoom.chatRoom.post));
//            System.out.println("--3--");
//            System.out.println(t.get(QChatRoom.chatRoom.seller));
//            System.out.println("--4--");
//            System.out.println(t.get(QChatRoom.chatRoom.buyer));
            System.out.println("--5--");
            System.out.println(t.get(QChat.chat.chatId.max()));
        });
    }
}
