package com.exercise.carrotproject.domain.chat.controller;

import com.exercise.carrotproject.domain.chat.dto.ChatRoomDto;
import com.exercise.carrotproject.domain.chat.entity.Chat;
import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.chat.entity.QChat;
import com.exercise.carrotproject.domain.chat.entity.QChatRoom;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRepository;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRoomRepository;
import com.exercise.carrotproject.domain.chat.service.ChatService;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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
import java.util.Optional;

import static com.exercise.carrotproject.web.common.SessionConst.LOGIN_MEMBER;
import static org.springframework.messaging.simp.stomp.StompHeaders.SESSION;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/post/{postId}")
    @Transactional
    public String chatStart(@PathVariable Long postId, HttpSession session, Model model) {
        Post post = em.find(Post.class, postId);
        Member seller = post.getMember();
        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);
        Member buyer = MemberEntityDtoMapper.toMemberEntity(memberDto);

        //기생성된 채팅방이 존재하면 redirect
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByPostAndSellerAndBuyer(post, seller, buyer);
        if (chatRoom.isPresent()) {
            return "redirect:/chat/chatRoom/"+chatRoom.get().getRoomId();
        }

        model.addAttribute("seller", seller.getMemId());
        model.addAttribute("buyer", buyer.getMemId());
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

    @GetMapping("/chatRoomListByPost/{postId}")
    public String chatRoomListByPost(Model model, @PathVariable Long postId) {
        //chatRoom select, where by postId

        //만약 생성된 채팅이 없으면 이동하지 않고 오류메세지 출력하도록 해야함. 그럼.. ajax 써야하나..??

        //html파일 만들어야함..
        return "chat/chatRoomListByPost";
    }

    @GetMapping("/chatRoomList")
    public String chatRoomList(Model model, HttpSession session) {
        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);
        Member memberEntity = MemberEntityDtoMapper.toMemberEntity(memberDto);

        List<Long> ids = jpaQueryFactory.select(QChat.chat.chatId.max())
                .from(QChat.chat)
                .groupBy(QChat.chat.room.roomId)
                .fetch();

        List<ChatRoomDto> chatRoomList = jpaQueryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                                QChatRoom.chatRoom.roomId,
                                QChatRoom.chatRoom.seller.memId.as("sellerId"),
                                QChatRoom.chatRoom.buyer.memId.as("buyerId"),
                                QChat.chat.message,
                                QChat.chat.createdTime,
                                ExpressionUtils.as(
                                        JPAExpressions.select(QChat.chat.chatId.count())
                                                .from(QChat.chat)
                                                .where(QChat.chat.room.eq(QChatRoom.chatRoom)
                                                        .and(QChat.chat.readState.eq(0))
                                                        .and(QChat.chat.from.ne(memberEntity))
                                                ),
                                        "unacknowledgedMessageCount"
                                )
                        )
                )
                .from(QChatRoom.chatRoom)
                .leftJoin(QChat.chat)
                .on(QChatRoom.chatRoom.roomId.eq(QChat.chat.room.roomId))
                .where(
                        (QChatRoom.chatRoom.seller.eq(memberEntity)
                            .or(QChatRoom.chatRoom.buyer.eq(memberEntity))
                        )
                        .and(QChat.chat.chatId.in(ids))
                )
                .orderBy(QChat.chat.createdTime.desc())
                .fetch();
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
        simpMessagingTemplate.convertAndSend("/topic/chatNoti/" + chat.getTo().getMemId(), "알람메세지");
        //채팅방으로 메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/chat/" + postId + "/" + seller + "/" + buyer, map);
        //채팅목록으로 메세지 보내기
        simpMessagingTemplate.convertAndSend("/topic/chatRoomList/" + chat.getTo().getMemId(), chat.getRoom().getRoomId());
    }

    //메시지 확인 처리 메소드
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

    @PostMapping("/getChatRoom/{roomId}")
    @ResponseBody
    public ChatRoomDto getChatRoom(@PathVariable Long roomId, HttpSession session){
        MemberDto memberDto = (MemberDto) session.getAttribute(LOGIN_MEMBER);
        Member memberEntity = MemberEntityDtoMapper.toMemberEntity(memberDto);

        Long maxChatId = jpaQueryFactory.select(QChat.chat.chatId.max())
                .from(QChat.chat)
                .where(QChat.chat.room.roomId.eq(roomId))
                .fetchOne();

        ChatRoomDto chatRoomDto = jpaQueryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                                QChatRoom.chatRoom.roomId,
                                QChatRoom.chatRoom.seller.memId.as("sellerId"),
                                QChatRoom.chatRoom.buyer.memId.as("buyerId"),
                                QChat.chat.message,
                                QChat.chat.createdTime,
                                ExpressionUtils.as(
                                        JPAExpressions.select(QChat.chat.chatId.count())
                                                .from(QChat.chat)
                                                .where(QChat.chat.room.eq(QChatRoom.chatRoom)
                                                        .and(QChat.chat.readState.eq(0))
                                                        .and(QChat.chat.from.ne(memberEntity))
                                                ),
                                        "unacknowledgedMessageCount"
                                )
                        )
                )
                .from(QChatRoom.chatRoom)
                .leftJoin(QChat.chat)
                .on(QChatRoom.chatRoom.roomId.eq(QChat.chat.room.roomId))
                .where(
                        QChatRoom.chatRoom.roomId.eq(roomId)
                                .and(QChat.chat.chatId.eq(maxChatId))
                )
                .fetchOne();

        return chatRoomDto;
    }
}
