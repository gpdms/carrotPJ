package com.exercise.carrotproject.web.review.form;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
