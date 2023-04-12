package com.exercise.carrotproject.domain.chat.entity;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.HideStateConverter;
import com.exercise.carrotproject.domain.converter.ReadStateConverter;
import com.exercise.carrotproject.domain.enumList.ReadState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME;

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

    @ManyToOne
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
    private Integer imgState = 0;

    @OneToMany(mappedBy = "chat")
    private List<ChatImg> chatImgList = new ArrayList<>();
}
