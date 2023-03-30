package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.converter.LocAttributeConverter;
import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.converter.CategoryConverter;
import com.exercise.carrotproject.domain.member.MemberRepository;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ColumnDefault("0")
    @Column(columnDefinition = "varchar(50)")
    private String title;
    @ManyToOne @JoinColumn(name = "mem_id") @JsonIgnore
    private Member member;
    @ColumnDefault("0") @Column(nullable = false)
    private Integer price;
    private String content;
    @Convert(converter = LocAttributeConverter.class)
    private Loc loc; //지역 enum
    @Convert(converter = CategoryConverter.class)
    private Category category; //카테고리 enum
    @ColumnDefault("0") @Column(nullable = false)
    private Integer hideState; //숨김여부: 0보임,1숨김
    @ColumnDefault("0") @Column(nullable = false)
    private Integer sellState; //판매여부: 0판매중,1예약중,2거래완료
    private Integer hits;
    private String wishPlace;



    //ColumnDefault, nullable=false는 데이터베이스에만 적용되고 영속성컨텍스트에는 null이기때문에
    //영속 상태 되기 이전에 실행하여 영속컨텍스트에도 담아줌.
    @PrePersist
    public void prePersist(){
        this.price = this.price == null ? 0 : this.price;
        this.hideState = this.hideState == null ? 0 : this.hideState;
        this.sellState = this.sellState == null ? 0 : this.sellState;
    }





}
