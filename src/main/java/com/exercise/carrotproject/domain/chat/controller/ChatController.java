package com.exercise.carrotproject.domain.chat.controller;

import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRepository;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRoomRepository;
import com.exercise.carrotproject.domain.chat.service.ChatService;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.MemberRepository;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.exercise.carrotproject.SessionConst.LOGIN_MEMBER;
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

    @PersistenceContext
    EntityManager em;

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
    public String chat(@PathVariable Long roomId, Model model){
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
    public int getChatNoti(HttpSession session){
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

    @GetMapping("/sessionTest")
    @ResponseBody
    public String sessionTest(HttpSession session) {
        Member member = memberRepository.findById("tester")
                .filter(m -> m.getMemPwd().equals("12345678"))
                .orElse(null);
        session.setAttribute(LOGIN_MEMBER, member);
        return "세션테스트입니다!! >>> " + session.getAttribute(LOGIN_MEMBER);
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

        //알림메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chat.getTo().getMemId(), "알람메세지");
        //채팅방으로 메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/chat/" + postId + "/" + seller + "/" + buyer, map);
    }
}
