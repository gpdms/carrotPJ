package com.exercise.carrotproject.chat.entity;

import com.exercise.carrotproject.member.Member;

import javax.persistence.*;
import java.util.List;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private Member from;

    @Column(name = "read_state")
    private Integer readState;

    @Column(name = "message")
    private String message;

    @Column(name = "img_state")
    private Integer imgState;

    @OneToMany(mappedBy = "chat")
    private List<ChatImg> chatImgList;
}
