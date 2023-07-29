package com.exercise.carrotproject.domain.member.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@ToString
public class Block {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long blockId;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="from_mem")
    private Member fromMem;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="to_mem")
    private Member toMem;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdTime;

    @Builder
    private Block(Member fromMem, Member toMem) {
        this.fromMem = fromMem;
        this.toMem = toMem;
    }

    public Long getBlockId() {
        return blockId;
    }
    public Member getFromMem() {
        return Member.builder().memId(fromMem.getMemId())
                .nickname(fromMem.getNickname())
                .loc(fromMem.getLoc())
                .build();
    }
    public Member getToMem() {
        return Member.builder().memId(toMem.getMemId())
                .nickname(toMem.getNickname())
                .loc(toMem.getLoc())
                .build();
    }
    public Timestamp getCreatedTime() {
        return createdTime;
    }
}
