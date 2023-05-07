package com.exercise.carrotproject.domain.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordId;
    private String keywordContent;

    @ManyToOne
    @JoinColumn(name = "mem_id")
    private Member member;
}
