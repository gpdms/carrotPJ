package com.exercise.carrotproject.domain.post.dto;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.MemberDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostDto {
    private Long postId;
    private String title;
    private String memberId;
    private Integer price;
    private String content;
    private Loc loc;
    private Category category;
    private Integer hideState;
    private Integer sellState;
    private Integer hits;
    private String wishPlace;






}
