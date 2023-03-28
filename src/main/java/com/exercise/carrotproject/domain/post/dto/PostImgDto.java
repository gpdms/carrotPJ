package com.exercise.carrotproject.domain.post.dto;

import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostImg;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostImgDto {
    private Long imgId;
    private Post post;
    private String orgName; //원본파일이름
    private String savedName; //저장된파일이름
    private String savedPath; //저장경로



}
