package com.exercise.carrotproject.domain.post.dto;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
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
    private Member member;
    private Integer price;
    private String content;
    private Loc loc;
    private Category category;
    private Integer hideState;
    private Integer sellState;
    private Integer hits;
    private String wishPlace;

    //Entity->Dto 변환
    public static PostDto entityToDto (Post post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .member(post.getMember())
                .price(post.getPrice())
                .content(post.getContent())
                .loc(post.getLoc())
                .category(post.getCategory())
                .hideState(post.getHideState())
                .sellState(post.getSellState())
                .hits(post.getHits())
                .wishPlace(post.getWishPlace())
                .build();
    }




}
