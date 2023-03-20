package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class BuyList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyId;
    @ManyToOne @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne @JoinColumn(name = "buyer_id")
    private Member buyer;
    @ManyToOne @JoinColumn(name = "seller_id")
    private Member seller;

}
