package com.exercise.carrotproject.web.member.form.memberInfo;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import com.querydsl.core.annotations.QueryProjection;
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

    //리뷰버튼 보이기 위한 것
    //Long reviewSellerId = reviewSellerService.findReviewSellerIdByPost(buyOne.getPost());
    private Long reviewBuyerId;

    @QueryProjection
    public MySellListForm(Long sellId, Post post, String sellerId, String buyerId, Long reviewBuyerId) {
        this.sellId = sellId;
        this.postDto = PostEntityDtoMapper.entityToDto(post);
        this.sellerId = sellerId;
        this.buyerId = buyerId;
    }
}
