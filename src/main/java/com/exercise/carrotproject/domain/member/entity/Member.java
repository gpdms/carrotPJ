package com.exercise.carrotproject.domain.member.entity;



import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.converter.LocAttributeConverter;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
<<<<<<< Updated upstream
=======
import com.fasterxml.jackson.annotation.JsonIgnore;
>>>>>>> Stashed changes
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonIgnoreProperties({"blockfromMemList", "blocktoMemList", "reviewBuyerList", "reviewSellerList"})
@ToString (exclude = {"blockfromMemList", "blocktoMemList", "reviewBuyerList", "reviewSellerList"})
@JsonIgnoreProperties({"blockfromMemList", "blocktoMemList", "reviewBuyerList", "reviewSellerList"})
@DynamicUpdate
public class Member extends BaseEntity {
    @Id
    @Size(min = 6, max = 12)
    private String memId;

    @Column(nullable = false)
    @Size(min = 8)
    private String memPwd;

    @Column(nullable = false)
    @Size(min = 2, max = 12)
    private String nickname;

    @Column(nullable = false)
//    @ColumnDefault("'D:/pf/profile_img.png'")
    @ColumnDefault("'/Users/img/pf/profile_img.png'")
    @Size(max=500)
    private String profPath;

    @Column(nullable = false)
    @ColumnDefault("36.5")
    private Double mannerScore;

    @Column(nullable = false)
    @Convert(converter = LocAttributeConverter.class)
    private Loc loc;

    @PrePersist
    public void createDefault() {
//        this.profPath = "D:/pf/profile_img.png";
        this.profPath= "/Users/img/pf/profile_img.png";
        this.mannerScore = 36.5;
    }

    public void updatePwd(String memPwd) {
        this.memPwd = memPwd;
    }
    public void updateProfile(String nickname, String profPath, Loc loc) {
        this.nickname = nickname;
        this.profPath = profPath;
        this.loc = loc;
    }

    //Block테이블
    @OneToMany(mappedBy="fromMem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Block> blockfromMemList = new ArrayList<>();
    @OneToMany(mappedBy="toMem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Block> blocktoMemList = new ArrayList<>();

    //reviewBuyer 테이블
    @OneToMany(mappedBy="buyer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewBuyer> reviewBuyerList = new ArrayList<>();
    //reviewSeller 테이블
    @OneToMany(mappedBy="seller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewSeller> reviewSellerList = new ArrayList<>();

}
