package com.exercise.carrotproject.chat.entity;

import javax.persistence.*;

@Entity
public class ChatImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_img_id")
    private Long chatImgId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @Column(name = "img_path")
    private String imgPath;
}
