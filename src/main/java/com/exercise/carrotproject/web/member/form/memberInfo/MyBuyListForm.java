package com.exercise.carrotproject.web.member.form.memberInfo;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MyBuyListForm {
    private Long buyId;
    private PostDto postDto;
    private String buyerId;
    private String sellerId;

    private Long reviewSellerId;

    public MyBuyListForm(Long buyId, Post post, String buyerId, String sellerId, Long reviewSellerId) {
        this.buyId = buyId;
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.reviewSellerId = reviewSellerId;
    }
}
