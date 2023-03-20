package com.exercise.carrotproject.domain.post.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PostImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @ManyToOne @JoinColumn(name = "post_id")
    private Post post;
    private String orgName; //원본파일이름
    private String savedName; //저장된파일이름
    private String savedPath; //저장경로

}
