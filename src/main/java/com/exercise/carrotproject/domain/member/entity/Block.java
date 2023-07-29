package com.exercise.carrotproject.domain.member.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
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
    @Column(updatable = false, nullable = false)
    private Timestamp createdTime;

    @Builder
    private Block(Member fromMem, Member toMem) {
        this.fromMem = fromMem;
        this.toMem = toMem;
    }
}
