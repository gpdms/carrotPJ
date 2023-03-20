package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.common.Loc;
import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "varchar(50)")
    private String title;
    @ManyToOne @JoinColumn(name = "mem_id")
    private Member member;
    private Integer price;
    private String content;
    //@Enumerated(EnumType.STRING) //enum이름 그대로 db에 저장됨.
    @NotNull
    @Convert(converter = LocConverter.class)
    private Loc loc; //지역 enum
    @Enumerated(EnumType.STRING)
//    private Category category; //카테고리 enum
    @ColumnDefault("0") @Column(nullable = false)
    private Integer hideState; //숨김여부: 0보임,1숨김
    @ColumnDefault("0") @Column(nullable = false)
    private Integer sellState; //판매여부: 0판매중,1예약중,2거래완료
    private Integer hits;

    //ColumnDefault, nullable=false는 데이터베이스에만 적용되고 영속성컨텍스트에는 null이기때문에
    //영속 상태 되기 이전에 실행
    @PrePersist
    public void prePersist(){
        this.hideState = this.hideState == null ? 0 : this.hideState;
        this.sellState = this.sellState == null ? 0 : this.sellState;
    }


}
