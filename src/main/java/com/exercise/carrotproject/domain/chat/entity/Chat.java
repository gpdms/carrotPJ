package com.exercise.carrotproject.domain.chat.entity;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.ImgStateConverter;
import com.exercise.carrotproject.domain.converter.ReadStateConverter;
import com.exercise.carrotproject.domain.enumList.ImgState;
import com.exercise.carrotproject.domain.enumList.ReadState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {"room","post","from","to","chatImgList"})
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private Member from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private Member to;

    @Column(name = "read_state",
            nullable = false)
    @ColumnDefault("0")
    @Builder.Default()
    @Convert(converter = ReadStateConverter.class)
    private ReadState readState = ReadState.NOTREAD;

    @Column(name = "message")
    private String message;

    @Column(name = "img_state",
            nullable = false)
    @ColumnDefault("0")
    @Builder.Default()
    @Convert(converter = ImgStateConverter.class)
    private ImgState imgState = ImgState.NOTATTACH;

    @OneToMany(mappedBy = "chat", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default()
    private List<ChatImg> chatImgList = new ArrayList<>();
}
