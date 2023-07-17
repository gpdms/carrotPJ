package com.exercise.carrotproject.domain.post.dto;

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
    // 판매완료한 게시글 목록 중에서, 남긴 리뷰의 Id : null일 때 남긴 리뷰 없음.
    private Long reviewBuyerId;

    @QueryProjection
    public SoldPostDto(Post post, Long reviewBuyerId) {
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.reviewBuyerId = reviewBuyerId;
    }
}
