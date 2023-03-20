package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;
    @ManyToOne @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne @JoinColumn(name = "mem_id")
    private Member member;
}
