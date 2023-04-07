package com.exercise.carrotproject.domain.post.dto;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class SellListDto {
    private Long sellId;
    //private Post post;
    private PostDto postDto;
    //private Member seller;
    private String sellerId;
    //private Member buyer;
    private String buyerId;

    //리뷰버튼 보이기 위한것
    private Long reviewBuyerId;

    public SellListDto(Long sellId, Post post, String sellerId, String buyerId, Long reviewBuyerId) {
        this.sellId = sellId;
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.reviewBuyerId = reviewBuyerId;
    }
}
