package com.exercise.carrotproject.domain.member.dto;


import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.LocAttributeConverter;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MemberDto {
    private String memId;
    private String nickname;
    private String email;
    private Loc loc;
    private String profPath;
    private Double mannerScore;
    private Role role;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private Date updatedTimeManner;

    /**
     * 매너점수 업데이트를 위해, 리뷰 테이블에서 리뷰점수를 가져올 때 사용한다.
     * @param memId
     * @param reviewScore
     */
    @QueryProjection
    public MemberDto(String memId, Double reviewScore) {
        this.memId = memId;
        this.mannerScore = reviewScore;
    }
}
