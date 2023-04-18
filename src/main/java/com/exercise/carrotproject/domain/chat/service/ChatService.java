package com.exercise.carrotproject.domain.chat.service;

import com.exercise.carrotproject.domain.chat.dto.ChatDto;
import com.exercise.carrotproject.domain.chat.dto.ChatRoomDto;
import com.exercise.carrotproject.domain.chat.dto.MessageDto;
import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.member.dto.MemberDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ChatService {
    //채팅 저장
    Chat saveChat(Long postId, String fromId, String toId, MessageDto message);

    //메세지 읽음 처리
    long updateChatReadState(Long roomId, String memId);
    
    //해당 방의 채팅 메세지 호출
//    Map<String, List<Chat>> getChatListByRoom(Long roomId);
    Map<String, List<ChatDto>> getChatListByRoom(Long roomId);

    //채팅방 존재 여부 확인
    Optional<ChatRoom> getChatRoomByPostAndSellerAndBuyer(Long postId, String sellerId, String buyerId);

    //미확인 메세지 개수 호출
    int getNotReadChatCnt(MemberDto memberDto);

    //해당 게시글에 생성된 채팅방 목록 호출
    List<ChatRoomDto> getChatRoomListByPost(MemberDto memberDto, Long postId);
    
    //모든 채팅방 목록 호출
    List<ChatRoomDto> getChatRoomList(MemberDto memberDto);

    //채팅방 호출 (채팅 목록에서 채팅방 비동기 갱신 용도)
    ChatRoomDto getChatRoom(MemberDto memberDto, Long roomId);
    
    //채팅 이미지 호출
    String getChatImgPath(Long chatImgId);
}
