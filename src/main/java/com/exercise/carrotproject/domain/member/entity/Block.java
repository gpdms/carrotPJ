package com.exercise.carrotproject.domain.member.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Block {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long blockId;

    @NotNull
    @ManyToOne
    @JoinColumn(name="from_mem")
    private Member fromMem;

    @NotNull
    @ManyToOne
    @JoinColumn(name="to_mem")
    private Member toMem;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdTime;

    public Long getBlockId() {
        return blockId;
    }
    public Member getFromMem() {
        return Member.builder().memId(fromMem.getMemId())
                .loc(fromMem.getLoc())
                .nickname(fromMem.getNickname())
                .mannerScore(fromMem.getMannerScore()).build();
    }
    public Member getToMem() {
        return Member.builder().memId(toMem.getMemId())
                .loc(toMem.getLoc())
                .nickname(toMem.getNickname())
                .mannerScore(toMem.getMannerScore()).build();
    }
    public Timestamp getCreatedTime() {
        return createdTime;
    }
}
