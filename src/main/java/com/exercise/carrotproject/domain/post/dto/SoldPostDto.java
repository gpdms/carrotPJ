package com.exercise.carrotproject.domain.post.dto;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.SellState;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SoldPostDto {
    private PostDto postDto;
    private Long reviewBuyerId;

    @QueryProjection
    public SoldPostDto(Post post, Long reviewBuyerId) {
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.reviewBuyerId = reviewBuyerId;
    }
}
