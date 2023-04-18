package com.exercise.carrotproject.domain.chat.service;

import com.exercise.carrotproject.domain.chat.dto.ChatRoomDto;
import com.exercise.carrotproject.domain.chat.dto.MessageDto;
import com.exercise.carrotproject.domain.chat.entity.*;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRepository;
import com.exercise.carrotproject.domain.chat.repoisitory.ChatRoomRepository;
import com.exercise.carrotproject.domain.enumList.ImgState;
import com.exercise.carrotproject.domain.enumList.ReadState;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Value("${file.postImg}")
    private String uploadPath;

    @Override
    @Transactional
    public Chat saveChat(Long postId, String fromId, String toId, MessageDto message) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.get();
        Optional<Member> from = memberRepository.findById(fromId);
        Optional<Member> to = memberRepository.findById(toId);

        //채팅방 체크로직
        Member seller;
        Member buyer;
        if (post.getMember().getMemId().equals(fromId)) {
            seller = from.get();
            buyer = to.get();
        } else {
            seller = to.get();
            buyer = from.get();
        }

        Optional<ChatRoom> optionalChatRoom = getChatRoomByPostAndSellerAndBuyer(postId, seller.getMemId(), buyer.getMemId());
        ChatRoom chatRoom;
        if (optionalChatRoom.isEmpty()) {
            //채팅방이 존재하지 않는 경우
            chatRoom = ChatRoom.builder()
                    .post(post)
                    .seller(seller)
                    .buyer(buyer)
                    .build();
        } else {
            //채팅방이 존재하는 경우
            chatRoom = optionalChatRoom.get();
        }

        //채팅생성
        Chat chat = Chat.builder()
                .post(post)
                .from(from.get())
                .to((to.get()))
                .message(message.getMessage())
                .build();

        chat.setRoom(chatRoom);
        chatRepository.save(chat);

        //이미지 저장
        if (message.getImgCode() != null) {
           chat = saveImg(chat, message.getImgCode());
        }

        return chat;
    }

    @Override
    public long updateChatReadState(Long roomId, String memId) {
        long updateResult = jpaQueryFactory.update(QChat.chat)
                .set(QChat.chat.readState, ReadState.READ)
                .where(QChat.chat.room.roomId.eq(roomId), QChat.chat.to.memId.eq(memId), QChat.chat.readState.eq(ReadState.NOTREAD))
                .execute();
        return updateResult;
    }

    @Override
    public Map<String, List<Chat>> getChatListByRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        List<Chat> chatList = chatRepository.findByRoom(chatRoom);

        //채팅 날짜별로 나누기
        Map<String, List<Chat>> chatSectionList = new HashMap<>();
        chatList.stream().forEach(chat->{
            Timestamp createdTime = chat.getCreatedTime();
            long time = createdTime.getTime();
//            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time);
            String format = new SimpleDateFormat("yyyy-MM-dd").format(time);
            if (chatSectionList.get(format) == null) {
                chatSectionList.put(format, new ArrayList<>());
                chatSectionList.get(format).add(chat);
            } else {
                chatSectionList.get(format).add(chat);
            }
        });

        return chatSectionList;
    }

    @Override
    public Optional<ChatRoom> getChatRoomByPostAndSellerAndBuyer(Long postId, String sellerId, String buyerId) {
        Optional<ChatRoom> optionalChatRoom = Optional.ofNullable(jpaQueryFactory.selectFrom(QChatRoom.chatRoom)
                .where(QChatRoom.chatRoom.post.postId.eq(postId)
                        .and(QChatRoom.chatRoom.seller.memId.eq(sellerId))
                        .and(QChatRoom.chatRoom.buyer.memId.eq(buyerId)))
                .fetchOne());
        return optionalChatRoom;
    }

    @Override
    public int getNotReadChatCnt(MemberDto memberDto) {
        Member memberEntity = MemberEntityDtoMapper.toMemberEntity(memberDto);
        List<Chat> chatList = chatRepository.findByToAndReadState(memberEntity, ReadState.NOTREAD);//미확인 메세지 조회
        return chatList.size();
    }

    @Override
    public List<ChatRoomDto> getChatRoomListByPost(MemberDto memberDto, Long postId) {

        Member memberEntity = MemberEntityDtoMapper.toMemberEntity(memberDto);

        List<Long> ids = jpaQueryFactory.select(QChat.chat.chatId.max())
                .from(QChat.chat)
                .where(QChat.chat.from.eq(memberEntity).or(QChat.chat.to.eq(memberEntity)))
                .groupBy(QChat.chat.room.roomId)
                .fetch();

        List<ChatRoomDto> chatRoomList = jpaQueryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                                QChatRoom.chatRoom.roomId,
                                QChatRoom.chatRoom.post.postId,
                                QChatRoom.chatRoom.seller.memId.as("sellerId"),
                                QChatRoom.chatRoom.buyer.memId.as("buyerId"),
                                QChat.chat.message,
                                QChat.chat.createdTime,
                                ExpressionUtils.as(
                                        JPAExpressions.select(QChat.chat.chatId.count())
                                                .from(QChat.chat)
                                                .where(QChat.chat.room.eq(QChatRoom.chatRoom)
                                                        .and(QChat.chat.readState.eq(ReadState.NOTREAD))
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
                                .and(QChatRoom.chatRoom.post.postId.eq(postId))
                                .and(QChat.chat.chatId.in(ids))
                )
                .orderBy(QChat.chat.createdTime.desc())
                .fetch();

        return chatRoomList;
    }

    @Override
    public List<ChatRoomDto> getChatRoomList(MemberDto memberDto) {

        Member memberEntity = MemberEntityDtoMapper.toMemberEntity(memberDto);

        List<Long> ids = jpaQueryFactory.select(QChat.chat.chatId.max())
                .from(QChat.chat)
                .where(QChat.chat.from.eq(memberEntity).or(QChat.chat.to.eq(memberEntity)))
                .groupBy(QChat.chat.room.roomId)
                .fetch();

        List<ChatRoomDto> chatRoomList = jpaQueryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                                QChatRoom.chatRoom.roomId,
                                QChatRoom.chatRoom.post.postId,
                                QChatRoom.chatRoom.seller.memId.as("sellerId"),
                                QChatRoom.chatRoom.buyer.memId.as("buyerId"),
                                QChat.chat.message,
                                QChat.chat.createdTime,
                                ExpressionUtils.as(
                                        JPAExpressions.select(QChat.chat.chatId.count())
                                                .from(QChat.chat)
                                                .where(QChat.chat.room.eq(QChatRoom.chatRoom)
                                                        .and(QChat.chat.readState.eq(ReadState.NOTREAD))
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

        return chatRoomList;
    }

    @Override
    public ChatRoomDto getChatRoom(MemberDto memberDto, Long roomId) {

        Member memberEntity = MemberEntityDtoMapper.toMemberEntity(memberDto);

        Long maxChatId = jpaQueryFactory.select(QChat.chat.chatId.max())
                .from(QChat.chat)
                .where(QChat.chat.room.roomId.eq(roomId))
                .fetchOne();

        ChatRoomDto chatRoomDto = jpaQueryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                                QChatRoom.chatRoom.roomId,
                                QChatRoom.chatRoom.post.postId,
                                QChatRoom.chatRoom.seller.memId.as("sellerId"),
                                QChatRoom.chatRoom.buyer.memId.as("buyerId"),
                                QChat.chat.message,
                                QChat.chat.createdTime,
                                ExpressionUtils.as(
                                        JPAExpressions.select(QChat.chat.chatId.count())
                                                .from(QChat.chat)
                                                .where(QChat.chat.room.eq(QChatRoom.chatRoom)
                                                        .and(QChat.chat.readState.eq(ReadState.NOTREAD))
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

    public Chat saveImg(Chat chat, List<String> imgCode) {
        List<ChatImg> chatImgList = chat.getChatImgList();
        System.out.println("saveImg 진입했음!! 이미지 사이즈는? >> " + imgCode.size());
        for (int i = 0; i < imgCode.size(); i++) {

            String[] split = imgCode.get(i).split(",");

            String extension; // if 문을 통해 확장자명을 정해줌
            if (split[0].equals("data:image/jpeg;base64")) {
                extension = "jpeg";
            } else if (split[0].equals("data:image/png;base64")){
                extension = "png";
            } else {
                extension = "jpg";
            }

            String base64Image = split[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image); // 바이트 코드를
            try {
                File tempFile = File.createTempFile("image", "." + extension); // createTempFile을 통해 임시 파일을 생성해준다. (임시파일은 지워줘야함)
                OutputStream outputStream = new FileOutputStream(tempFile);
                outputStream.write(imageBytes); // OutputStream outputStream = new FileOutputStream(tempFile)을 통해 생성한 outputStream 객체에 imageBytes를 작성해준다.
                outputStream.flush();//커밋해주고
                outputStream.close();//종료해준다

                String saveName = UUID.randomUUID().toString(); // uuid를 통해 파일명이 겹치지 않게 해준다
                String savePath = uploadPath + File.separator + "chat" + File.separator + chat.getChatId() + File.separator + saveName + "." + extension;
                File saveFile = new File(savePath);
                FileUtils.moveFile(tempFile, saveFile);//저장경로에 존재하지 않는 폴더가 있으면 자동으로 생성해줌
                ChatImg chatImg = ChatImg.builder()
                        .chat(chat)
                        .imgPath(saveFile.getPath())
                        .build();
                chatImgList.add(chatImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        chat.setImgState(ImgState.ATTACH);
        return chatRepository.save(chat);
    }
}