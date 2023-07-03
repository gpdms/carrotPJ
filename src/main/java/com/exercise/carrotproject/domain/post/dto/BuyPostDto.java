package com.exercise.carrotproject.domain.post.dto;

import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class BuyPostDto {
    private Long tradeId;
    private PostDto postDto;
    private String buyerId;
    private String sellerId;
    //구매한 게시글 목록 중에서 남긴 리뷰의 Id : null일 때 남긴 리뷰 없음.
    private Long reviewSellerId;

    @Builder
    @QueryProjection
    public BuyPostDto(Long tradeId, Post post, String buyerId, String sellerId, Long reviewSellerId) {
        this.tradeId = tradeId;
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.reviewSellerId = reviewSellerId;
    }
}
