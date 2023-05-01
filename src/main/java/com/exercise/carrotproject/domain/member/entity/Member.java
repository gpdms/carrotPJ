package com.exercise.carrotproject.domain.member.entity;



import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.converter.LocAttributeConverter;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"blockfromMemList", "blocktoMemList", "reviewBuyerList", "reviewSellerList"})
@ToString (exclude = {"blockfromMemList", "blocktoMemList", "reviewBuyerList", "reviewSellerList"})
@DynamicInsert
public class Member extends BaseEntity {
    @Id
    @Size(min = 6, max = 40)
    private String memId;

    //@NotNull
    @Size(min = 8)
    private String memPwd;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    @NotNull
    @Size(min = 2, max = 15)
    private String nickname;

    @Size(max=500)
    private String profPath;

    @Column(nullable = false)
    @ColumnDefault("365000.0")
    @DecimalMax("999000.0")
    private Double mannerScore;

    @NotNull
    @Convert(converter = LocAttributeConverter.class)
    private Loc loc;

    @PrePersist
    public void createDefault() {
        this.mannerScore = 365000.0;
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

    public String getMemId() {
        return new String(memId);
    }
    public String getMemPwd() {
        return new String(memPwd);
    }
    public String getNickname() {
        return nickname;
    }
    public String getProfPath() {
        return profPath;
    }
    public Double getMannerScore() {
        return mannerScore;
    }
    public Loc getLoc() {
        return loc;
    }

    public Role getRole() {
        return role;
    }

    public List<Block> getBlockfromMemList() {
        return blockfromMemList.stream().
                collect(Collectors.toUnmodifiableList());
    }
    public List<Block> getBlocktoMemList() {
        return blocktoMemList.stream().
                collect(Collectors.toUnmodifiableList());
    }
    public List<ReviewBuyer> getReviewBuyerList() {
        return reviewBuyerList.stream().
                collect(Collectors.toUnmodifiableList());
    }
    public List<ReviewSeller> getReviewSellerList() {
        return reviewSellerList.stream().
                collect(Collectors.toUnmodifiableList());
    }


}
