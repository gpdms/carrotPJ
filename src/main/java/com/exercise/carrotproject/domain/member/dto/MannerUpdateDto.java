package com.exercise.carrotproject.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MannerUpdateDto {
    private String memId;
    private Double reviewScore;

    /**
     * 매너점수 업데이트를 위해, 리뷰 테이블에서 리뷰점수를 가져올 때 사용한다.
     * @param memId
     * @param reviewScore
     */
    @QueryProjection
    public MannerUpdateDto(String memId, Double reviewScore) {
        this.memId = memId;
        this.reviewScore= reviewScore;
    }
}
