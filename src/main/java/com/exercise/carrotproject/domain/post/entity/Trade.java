package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.converter.HideStateConverter;
import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TradeId;
    @ManyToOne @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne @JoinColumn(name = "buyer_id")
    private Member buyer;
    @ManyToOne @JoinColumn(name = "seller_id")
    private Member seller;
    @ColumnDefault("0") @Column(nullable = false)
    @Convert(converter = HideStateConverter.class)
    private HideState hideStateBuyer; //숨김여부: 0보임,1숨김
}
