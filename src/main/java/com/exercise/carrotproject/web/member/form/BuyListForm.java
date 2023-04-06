package com.exercise.carrotproject.web.member.form;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class BuyListForm {
    private Long buyId;
    private PostDto postDto;
    private String buyerId;
    private String sellerId;

    private boolean isRegisteredReview;

    public BuyListForm(Long buyId, Post post, String buyerId, String sellerId) {
        this.buyId = buyId;
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }
}
