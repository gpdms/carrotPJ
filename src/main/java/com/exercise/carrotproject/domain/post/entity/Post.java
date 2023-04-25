package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.HideStateConverter;
import com.exercise.carrotproject.domain.converter.LocAttributeConverter;
import com.exercise.carrotproject.domain.converter.SellStateConverter;
import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.converter.CategoryConverter;
import com.exercise.carrotproject.domain.enumList.SellState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Post extends BaseEntity {
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
    private Category category; //enum

    @ColumnDefault("0") @Column(nullable = false)
    @Convert(converter = HideStateConverter.class)
    private HideState hideState; //enum

    @ColumnDefault("0") @Column(nullable = false)
    @Convert(converter = SellStateConverter.class)
    private SellState sellState; //enum

    private Integer hits;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostImg> postImgList = new ArrayList<>();
    @OneToOne(mappedBy = "post", cascade = CascadeType.REMOVE)
    private MtPlace mtPlace;

    //ColumnDefault, nullable=false는 데이터베이스에만 적용되고 영속성컨텍스트에는 null이기때문에
    //영속 상태 되기 이전에 실행하여 영속컨텍스트에도 담아줌.
    @PrePersist
    public void prePersist(){
        this.price = this.price == null ? 0 : this.price;
        this.hideState = this.hideState == null ? HideState.SHOW : this.hideState;
        this.sellState = this.sellState == null ? SellState.ON_SALE : this.sellState;
    }
}
