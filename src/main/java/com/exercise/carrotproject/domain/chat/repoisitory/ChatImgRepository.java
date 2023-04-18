package com.exercise.carrotproject.domain.chat.repoisitory;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatImg;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.enumList.ReadState;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatImgRepository extends JpaRepository<ChatImg, Long> {
}
