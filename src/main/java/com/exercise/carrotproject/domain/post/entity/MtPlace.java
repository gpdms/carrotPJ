package com.exercise.carrotproject.domain.post.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MtPlace {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mtPlaceId;

    @OneToOne
    private Post post;
    private Double lat; //위도
    private Double lon; //경도
    private String placeInfo;
}
