package com.exercise.carrotproject.domain.member.dto;


import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.LocAttributeConverter;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
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
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString (exclude = {"blockfromMemList", "blocktoMemList", "reviewBuyerList", "reviewSellerList"})
public class MemberDto {
    private String memId;
    //private String memPwd;
    private String nickname;
    //private String profPath;
    private Double mannerScore;
    private Loc loc;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private Role role;

    public MemberDto(String mem_id, Double mannerScore) {
        this.memId = mem_id;
        this.mannerScore = mannerScore;
    }
}
