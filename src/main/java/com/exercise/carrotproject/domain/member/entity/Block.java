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
@Getter
@Builder
@ToString
@DynamicInsert
public class Block {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long blockId;

    @ManyToOne
    @JoinColumn(name="from_mem", nullable = false)
    private Member fromMem;

    @ManyToOne
    @JoinColumn(name="to_mem", nullable = false)
    private Member toMem;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdTime;
}
