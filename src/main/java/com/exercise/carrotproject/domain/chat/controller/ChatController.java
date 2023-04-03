package com.exercise.carrotproject.domain.chat.controller;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.chat.entity.QChat;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRepository;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRoomRepository;
import com.exercise.carrotproject.domain.chat.service.ChatService;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.exercise.carrotproject.web.common.SessionConst.LOGIN_MEMBER;
import static org.springframework.messaging.simp.stomp.StompHeaders.SESSION;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final MemberRepository memberRepository;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    @GetMapping("/post/{postId}")
    public String chatStart(@PathVariable Long postId, HttpSession session, Model model) {
        Post post = em.find(Post.class, postId);
        String seller = post.getMember().getMemId();
        String buyer = ((MemberDto) session.getAttribute(LOGIN_MEMBER)).getMemId();
        model.addAttribute("seller", seller);
        model.addAttribute("buyer", buyer);
        return "chat/chat";
    }

    @GetMapping("/chatRoom/{roomId}")
    @Transactional
    public String chat(@PathVariable Long roomId, Model model, HttpSession session) {
        getChatMsg(roomId, session);//미확인메세지 업데이트
        ChatRoom chatRoom = em.find(ChatRoom.class, roomId);
        List<Chat> chatList = chatRepository.findByRoom(chatRoom);
        String seller = chatRoom.getSeller().getMemId();
        String buyer = chatRoom.getBuyer().getMemId();
        model.addAttribute("postId", chatRoom.getPost().getPostId());
        model.addAttribute("seller", seller);
        model.addAttribute("buyer", buyer);
        model.addAttribute("chatList", chatList);
        return "chat/chat";
    }

    @PostMapping("/getChatNoti")
    @ResponseBody
    public int getChatNoti(HttpSession session) {
        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);
        Member memberEntity = MemberEntityDtoMapper.toMemberEntity(memberDto);
        List<Chat> chatList = chatRepository.findByToAndReadState(memberEntity, 0);//미확인 메세지 조회
        return chatList.size();
    }

    @GetMapping("/chatRoomList")
    public String chatRoomList(Model model, HttpSession session) {
        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);
        Member memberEntity = MemberEntityDtoMapper.toMemberEntity(memberDto);
        List<ChatRoom> chatRoomList = chatRoomRepository.findBySellerOrBuyer(memberEntity, memberEntity); //seller or buyer가 유저 아이디와 같은 채팅방 찾아야함
        model.addAttribute("chatRoomList", chatRoomList);
        return "chat/chatRoomList";
    }

    @MessageMapping("/chat/{postId}/{seller}/{buyer}")
    public void chat(
            @DestinationVariable Long postId,
            @DestinationVariable String seller,
            @DestinationVariable String buyer,
            @Payload String message,
            SimpMessageHeaderAccessor simpMessageHeaderAccessor) {

        Post post = em.find(Post.class, postId);
        Member sellerEntity = em.find(Member.class, seller);
        Member buyerEntity = em.find(Member.class, buyer);

        //보내는이
        MemberDto memberDto = (MemberDto) simpMessageHeaderAccessor.getSessionAttributes().get(SESSION);
        Chat chat;
        if (memberDto.getMemId().equals(seller)) {
            chat = Chat.builder()
                    .post(post)
                    .from(sellerEntity)
                    .to(buyerEntity)
                    .message(message)
                    .build();
        } else {
            chat = Chat.builder()
                    .post(post)
                    .from(buyerEntity)
                    .to(sellerEntity)
                    .message(message)
                    .build();
        }
        //채팅 저장
        chat = chatService.saveChat(chat, sellerEntity, buyerEntity);

        Map map = new HashMap();
        map.put("chat", chat.getMessage()); //채팅메세지
        map.put("from", memberDto.getMemId()); //보내는이
        map.put("room", chat.getRoom().getRoomId()); //채팅방 id

        //알림메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chat.getTo().getMemId(), "알람메세지");
        //채팅방으로 메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/chat/" + postId + "/" + seller + "/" + buyer, map);
    }

    @PostMapping("/getChatMsg/{roomId}")
    @ResponseBody
    @Transactional
    public String getChatMsg(@PathVariable Long roomId, HttpSession session) {
        long updateResult = jpaQueryFactory.update(QChat.chat)
                .set(QChat.chat.readState, 1)
                .where(QChat.chat.room.roomId.eq(roomId), QChat.chat.to.memId.eq(((MemberDto) session.getAttribute(LOGIN_MEMBER)).getMemId()), QChat.chat.readState.eq(0))
                .execute();
        return "성공!";
    }
}
