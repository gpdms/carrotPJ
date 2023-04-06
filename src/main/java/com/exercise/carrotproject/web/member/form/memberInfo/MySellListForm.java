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
public class MySellListForm {
    private Long sellId;
    private PostDto postDto;
    private String sellerId;
    private String buyerId;

    private Long reviewBuyerId;

    public MySellListForm(Long sellId, Post post, String sellerId, String buyerId, Long reviewBuyerId) {
        this.sellId = sellId;
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.reviewBuyerId = reviewBuyerId;
    }
}
