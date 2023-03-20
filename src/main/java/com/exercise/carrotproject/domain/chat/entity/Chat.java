package com.exercise.carrotproject.domain.chat.entity;

import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
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
