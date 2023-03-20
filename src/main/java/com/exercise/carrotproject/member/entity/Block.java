package com.exercise.carrotproject.member.entity;

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
@Setter
@Builder
@ToString
@DynamicInsert
public class Block {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long blockId;

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
}
