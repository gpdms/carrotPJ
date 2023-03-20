package com.exercise.carrotproject.domain.chat.entity;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @OneToMany(mappedBy = "room")
    private List<Chat> chatList;

    @OneToMany(mappedBy = "room")
    private List<ChatImg> chatImgList;
}
