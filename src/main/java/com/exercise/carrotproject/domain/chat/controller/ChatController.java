package com.exercise.carrotproject.domain.chat.controller;

import com.exercise.carrotproject.domain.chat.dto.ChatDto;
import com.exercise.carrotproject.domain.chat.dto.ChatRoomDto;
import com.exercise.carrotproject.domain.chat.dto.MessageDto;
import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.chat.service.ChatService;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.exercise.carrotproject.web.common.SessionConst.LOGIN_MEMBER;
import static org.springframework.messaging.simp.stomp.StompHeaders.SESSION;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/post/{postId}/{sellerId}")
    @Transactional
    public String chatStart(
            @PathVariable Long postId,
            @PathVariable String sellerId,
            HttpSession session,
            Model model) {

        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);
        String buyerId = memberDto.getMemId();

        //기생성된 채팅방이 존재하면 redirect
        Optional<ChatRoom> optionalChatRoom = chatService.getChatRoomByPostAndSellerAndBuyer(postId, sellerId, buyerId);
        if (optionalChatRoom.isPresent()) {
            ChatRoom chatRoom = optionalChatRoom.get();
            return "redirect:/chat/chatRoom/"+ chatRoom.getRoomId() + "/" + chatRoom.getPost().getPostId() + "/" + chatRoom.getSeller().getMemId() + "/" + chatRoom.getBuyer().getMemId();
        }

        model.addAttribute("buyerId", buyerId);
        return "chat/chat";
    }

    @GetMapping("/chatRoom/{roomId}/{postId}/{sellerId}/{buyerId}")
    @Transactional
    public String chat(
            @PathVariable Long roomId,
            @PathVariable Long postId,
            @PathVariable String sellerId,
            @PathVariable String buyerId,
            Model model,
            HttpSession session) {

        getChatMsg(roomId, postId, sellerId, buyerId, session); //채팅 확인 업데이트

        Map<String, List<ChatDto>> chatSectionList = chatService.getChatListByRoom(roomId);

        model.addAttribute("chatSectionList", chatSectionList);

        return "chat/chat";
    }

    @PostMapping("/getChatNoti")
    @ResponseBody
    public int getChatNoti(HttpSession session) {

        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);
        int notReadChatCnt = chatService.getNotReadChatCnt(memberDto);

        return notReadChatCnt;
    }

    @GetMapping("/chatRoomListByPost/{postId}")
    public String chatRoomListByPost(Model model, HttpSession session, @PathVariable Long postId) {
        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);

        List<ChatRoomDto> chatRoomList = chatService.getChatRoomListByPost(memberDto, postId);

        model.addAttribute("chatRoomList", chatRoomList);

        //만약 생성된 채팅이 없으면 채팅목록에 아무것도 없는 빈페이지로 이동함
        //그러므로 채팅이 없는 경우에는 페이지 이동하지 않고 오류메세지 출력하도록 해야함. 그럼.. ajax 써야하나..??

        return "chat/chatRoomListByPost";
    }

    @GetMapping("/chatRoomList")
    public String chatRoomList(Model model, HttpSession session) {

        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);

        List<ChatRoomDto> chatRoomList = chatService.getChatRoomList(memberDto);

        model.addAttribute("chatRoomList", chatRoomList);

        return "chat/chatRoomList";
    }

    //채팅저장 메소드
    @MessageMapping("/chat/{postId}/{sellerId}/{buyerId}")
    public void chat(
            @DestinationVariable Long postId,
            @DestinationVariable String sellerId,
            @DestinationVariable String buyerId,
            @Payload MessageDto message,
            SimpMessageHeaderAccessor simpMessageHeaderAccessor) {

        MemberDto memberDto = (MemberDto) simpMessageHeaderAccessor.getSessionAttributes().get(SESSION);
        String sessionId = memberDto.getMemId();
        Chat chat;
        if(sessionId.equals(sellerId)){
            //from : seller / to : buyer
            chat = chatService.saveChat(postId, sellerId, buyerId, message);
        } else {
            //from : buyer / to : seller
            chat = chatService.saveChat(postId, buyerId, sellerId, message);
        }

        Map map = new HashMap();
        map.put("chat", chat.getMessage()); //채팅메세지
        map.put("from", memberDto.getMemId()); //보내는이
        map.put("room", chat.getRoom().getRoomId()); //채팅방 id
        map.put("createdTime", chat.getCreatedTimeByString()); //보낸시간
        map.put("readState", chat.getReadState().getReadStateName()); //읽음여부
        map.put("imgState", chat.getImgState().getImgStateCode());//이미지여부
        map.put("imgList", chat.getChatImgList());//이미지리스트

        //알림메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/chatNoti/" + chat.getTo().getMemId(), "알람메세지");
        //채팅방으로 메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/chat/" + postId + "/" + sellerId + "/" + buyerId, map);
        //채팅목록으로 메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/chatRoomList/" + chat.getTo().getMemId(), chat.getRoom().getRoomId());
    }

    //메시지 확인 처리 메소드
    @PostMapping("/getChatMsg/{roomId}/{postId}/{sellerId}/{buyerId}")
    @ResponseBody
    @Transactional
    public long getChatMsg(
            @PathVariable Long roomId,
            @PathVariable Long postId,
            @PathVariable String sellerId,
            @PathVariable String buyerId,
            HttpSession session) {
        String memId = ((MemberDto) session.getAttribute(LOGIN_MEMBER)).getMemId();
        long updateChatReadStateResult = chatService.updateChatReadState(roomId, memId);

        //해당 방으로 메세지 확인 완료 메세지 보내기
        Map map = new HashMap();
        map.put("readComplete", "readComplete");
        simpMessagingTemplate.convertAndSend("/topic/chat/" + postId + "/" + sellerId + "/" + buyerId, map);

        return updateChatReadStateResult;
    }

    @PostMapping("/getChatRoom/{roomId}")
    @ResponseBody
    public ChatRoomDto getChatRoom(@PathVariable Long roomId, HttpSession session){
        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);

        ChatRoomDto chatRoomDto = chatService.getChatRoom(memberDto, roomId);

        return chatRoomDto;
    }

    //채팅 이미지 출력
    @ResponseBody
    @GetMapping("/chatImg/{chatImgId}")
    public Resource viewProfileImg(@PathVariable("chatImgId") Long chatImgId) throws IOException {
        String imgPath = chatService.getChatImgPath(chatImgId);
        UrlResource urlResource = new UrlResource("file:" + imgPath);
        return urlResource;
    }
}
