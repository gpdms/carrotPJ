package com.exercise.carrotproject.domain.member.entity;

import com.exercise.carrotproject.domain.enumList.NotiKind;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notiId;

    @ManyToOne
    @JoinColumn(name = "mem_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private NotiKind notiKind;

    private String notiContent;
    private Integer readState;
    private Timestamp notiDate;
}
