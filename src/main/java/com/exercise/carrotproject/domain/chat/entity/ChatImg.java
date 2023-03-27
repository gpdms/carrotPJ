package com.exercise.carrotproject.domain.chat.entity;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ChatImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_img_id")
    private Long chatImgId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "img_path")
    private String imgPath;
}
