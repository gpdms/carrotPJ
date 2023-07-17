package com.exercise.carrotproject.domain.post.dto;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.Loc;

import com.exercise.carrotproject.domain.enumList.SellState;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostDto {
    private Long postId;
    private String title;
    private MemberDto member;
    private Integer price;
    private String content;
    private Loc loc;
    private Category category;
    private HideState hideState;
    private SellState sellState;
    private Integer hits;
    private String wishPlace;
    private String createdTime;
}
