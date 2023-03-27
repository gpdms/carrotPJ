package com.exercise.carrotproject.domain.chat.service;

import com.exercise.carrotproject.domain.chat.entity.Chat;

public interface ChatService {
    //채팅 저장
    public Chat saveChat(Chat chat);
}
