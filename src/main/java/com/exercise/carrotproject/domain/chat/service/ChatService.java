package com.exercise.carrotproject.domain.chat.service;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.member.entity.Member;

public interface ChatService {
    //채팅 저장
    public Chat saveChat(Chat chat, Member seller, Member buyer);
}
