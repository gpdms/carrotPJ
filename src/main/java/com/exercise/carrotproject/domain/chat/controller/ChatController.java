package com.exercise.carrotproject.domain.chat.controller;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRepository;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRoomRepository;
import com.exercise.carrotproject.domain.chat.service.ChatService;
import com.exercise.carrotproject.domain.member.MemberRepository;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.exercise.carrotproject.SessionConst.LOGIN_MEMBER;
import static org.springframework.messaging.simp.stomp.StompHeaders.SESSION;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MemberRepository memberRepository;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PersistenceContext
    EntityManager em;

    @GetMapping("/chat/{roomId}")
    public String chatTest(Model model, @PathVariable Long roomId) {
        ChatRoom chatRoom = em.find(ChatRoom.class, roomId);
        Optional<List<Chat>> chatList = chatRepository.findByRoom(chatRoom);
        if(chatList.isPresent()){
            model.addAttribute("chatList", chatList.get());
        }
        model.addAttribute("roomId", roomId);

        return "chat/chatTest";
    }

    @GetMapping("/chatRoomList")
    public String chatRoomList(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute(LOGIN_MEMBER);
        List<ChatRoom> chatRoomList = chatRoomRepository.findBySellerOrBuyer(member, member); //seller or buyer가 유저 아이디와 같은 채팅방 찾아야함
        model.addAttribute("chatRoomList", chatRoomList);
        return "chat/chatRoomList";
    }

    @GetMapping("/sessionTest")
    @ResponseBody
    public String sessionTest(HttpSession session) {
        Member member = memberRepository.findById("tester")
                .filter(m -> m.getMemPwd().equals("12345678"))
                .orElse(null);
        session.setAttribute(LOGIN_MEMBER, member);
        return "세션테스트입니다!! >>> " + session.getAttribute(LOGIN_MEMBER);
    }

    @MessageMapping("/chat/{roomId}")
    @Transactional
    public void chat(
            @DestinationVariable Long roomId,
            @Payload String message,
            SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        ChatRoom chatRoom = em.find(ChatRoom.class, roomId);
        String seller = chatRoom.getSeller().getMemId();
        String buyer = chatRoom.getBuyer().getMemId();
        Member member = (Member) simpMessageHeaderAccessor.getSessionAttributes().get(SESSION);
        Post post = em.find(Post.class, chatRoom.getPost().getPostId());
        Chat chat = Chat.builder()
                .post(post)
                .from(member)
                .message(message)
                .build();
        chat = chatService.saveChat(chat);

        Map map = new HashMap();
        map.put("chat", chat.getMessage()); //채팅메세지
        map.put("from", member.getMemId()); //보내는이

        //알림메세지 보내기
        if (seller.equals(member.getMemId())) {
            //판매자가 구매자에게 메세지를 보내는 경우
            simpMessagingTemplate.convertAndSend("/topic/" + buyer, "알람메세지");
        } else {
            //구매자가 판매자에게 메세지를 보내는 경우
            simpMessagingTemplate.convertAndSend("/topic/" + seller, "알람메세지");
        }

        //채팅방으로 메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/" + roomId, map);
    }
}
