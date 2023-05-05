package com.exercise.carrotproject.web.review.form;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ReviewForm {
    @NotNull
    private String buyerId;
    private String buyerNick;
    @NotNull
    private String sellerId;
    private String sellerNick;
    @NotNull
    private Long postId;
    @NotNull
    private String reviewStateCode;
    private List<String> indicators;
    private String message;

}
