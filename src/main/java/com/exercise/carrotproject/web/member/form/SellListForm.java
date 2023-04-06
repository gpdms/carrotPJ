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
public class SellListForm {
    private Long sellId;

    //private Post post;
    private PostDto postDto;
    //private Member seller;
    private String sellerId;
    //private Member buyer;
    private String buyerId;
    private Boolean reviewExist;

    public SellListForm(Long sellId, Post post, String buyerId, String sellerId, boolean reviewExist) {
        this.sellId = sellId;
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.reviewExist = reviewExist;
    }
}
