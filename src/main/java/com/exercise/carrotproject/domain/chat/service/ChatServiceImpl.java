package com.exercise.carrotproject.domain.chat.service;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRepository;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public Chat saveChat(Chat chat) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByPostAndBuyer(chat.getPost(), chat.getFrom());
        ChatRoom saveChatRoom = null;
        if (chatRoom.isEmpty()) {
            System.out.println(">>>> 채팅방이 존재하지 않는 경우입니다.");
            saveChatRoom = ChatRoom.builder()
                    .post(chat.getPost())
                    .seller(chat.getPost().getMember())
                    .buyer(chat.getFrom())
                    .build();
        } else {
            System.out.println(">>>> 채팅방이 존재하는 경우입니다.");
            saveChatRoom = chatRoom.get();
        }

        chat.setRoom(saveChatRoom);
        saveChatRoom.getChatList().add(chat);
        chatRoomRepository.save(saveChatRoom);

        return saveChatRoom.getChatList().get(saveChatRoom.getChatList().size()-1);
    }
}
