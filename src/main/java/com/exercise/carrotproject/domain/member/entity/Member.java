package com.exercise.carrotproject.domain.member.entity;



import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.converter.LocAttributeConverter;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@DynamicInsert
public class Member extends BaseEntity {
    @Id
    @Size(min = 6, max = 12)
    private String memId;

    @NotNull
    @Size(min = 8)
    private String memPwd;

    @Size(max = 12)
    private String nickname;

    @NotNull
    @ColumnDefault("'D:/pf/profile_img.png'") // String은 ''로 감싸줘야 한다.
    @Size(max=1000)
    private String profPath;

    @NotNull
    @ColumnDefault("36.5")
    private Double mannerScore;

    @NotNull
    @Convert(converter = LocAttributeConverter.class)
    private Loc loc;

    @PrePersist
    public void createDefault() {
        this.profPath= "D:/pf/profile_img.png";
        this.mannerScore = 36.5;
    }

    //Block테이블
    @OneToMany(mappedBy="fromMem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Block> blockfromMemList;
    @OneToMany(mappedBy="toMem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Block> blocktoMemList;

    //reviewBuyer 테이블
    @OneToMany(mappedBy="buyer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewBuyer> reviewBuyerList;
    //reviewSeller 테이블
    @OneToMany(mappedBy="seller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewSeller> reviewSellerList;

}
