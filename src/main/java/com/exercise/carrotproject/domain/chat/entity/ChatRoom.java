package com.exercise.carrotproject.domain.chat.entity;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString(exclude = {"chatList"})
@Table(name = "chat_room")
public class ChatRoom extends BaseEntity {
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

    @OneToMany(mappedBy = "room", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<Chat> chatList = new ArrayList<>();
}
