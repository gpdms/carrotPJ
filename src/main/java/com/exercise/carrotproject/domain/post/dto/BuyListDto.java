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
public class BuyListDto {
    private Long buyId;

    //private Post post;
    private PostDto postDto;
    //private Member buyer;
    private String buyerId;
    //private Member seller;
    private String sellerId;

    public BuyListDto(Long buyId, Post post, String buyerId, String sellerId) {
        this.buyId = buyId;
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }
}
