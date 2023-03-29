package com.exercise.carrotproject.domain.chat.repoisitory;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByToAndReadState(Member member, int readState);//읽지 않은 메세지 조회

    List<Chat> findByRoom(ChatRoom chatRoom);//채팅방 메세지 조회
}
