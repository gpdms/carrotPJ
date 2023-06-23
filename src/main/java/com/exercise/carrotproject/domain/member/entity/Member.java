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
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"blockfromMemList", "blocktoMemList", "reviewBuyerList", "reviewSellerList"})
@ToString (exclude = {"blockfromMemList", "blocktoMemList", "reviewBuyerList", "reviewSellerList"})
@DynamicInsert
public class Member extends BaseEntity {
    @Id @Column(updatable = false) @Size(min = 6, max = 40)
    private String memId;
    @Size(min = 8)
    private String memPwd;
    @NotNull @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    @NotNull @Column(unique = true) @Size(min = 2, max = 15)
    private String nickname;
    @Size(max=500)
    private String profPath;
    @NotNull @ColumnDefault("365000") @Range(min = 0, max = 1200000)
    @Column(columnDefinition = "double precision CHECK (manner_score >= 0 AND manner_score <= 1200000)")
    private Double mannerScore;
    @NotNull @Convert(converter = LocAttributeConverter.class)
    private Loc loc;
    @Column(insertable = false) @Temporal(TemporalType.TIMESTAMP)
    private Date updatedMannerScoreTime;

    @OneToMany(mappedBy="fromMem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Block> blockFromMemList = new ArrayList<>();
    @OneToMany(mappedBy="toMem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Block> blockToMemList = new ArrayList<>();
    @OneToMany(mappedBy="buyer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewBuyer> reviewBuyerList = new ArrayList<>();
    @OneToMany(mappedBy="seller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewSeller> reviewSellerList = new ArrayList<>();

    public String getMemId() {
        return memId;
    }
    public String getMemPwd() {
        return memPwd;
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
    public List<Block> getBlockFromMemList() {
        return Collections.unmodifiableList(blockFromMemList);
    }
    public List<Block> getBlocktoMemList() {
        return Collections.unmodifiableList(blockToMemList);
    }
    public List<ReviewBuyer> getReviewBuyerList() {
        return Collections.unmodifiableList(reviewBuyerList);
    }
    public List<ReviewSeller> getReviewSellerList() {
        return Collections.unmodifiableList(reviewSellerList);
    }

    @PrePersist
    public void prePersistMember() {
        this.mannerScore = 365000.0;
    }

    public void updateMemPwd(String memPwd) {
        this.memPwd = memPwd;
    }
    public void updateProfile(Member updateMember) {
        this.nickname = updateMember.getNickname();
        this.profPath = updateMember.getProfPath();
        this.loc = updateMember.getLoc();
    }
}

