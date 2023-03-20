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
public class SellList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellId;
    @ManyToOne @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne @JoinColumn(name = "seller_id")
    private Member seller;
    @ManyToOne @JoinColumn(name = "buyer_id")
    private Member Buyer;
}
