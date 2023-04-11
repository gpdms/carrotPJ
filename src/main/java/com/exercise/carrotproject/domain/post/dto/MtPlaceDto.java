package com.exercise.carrotproject.domain.post.dto;

import com.exercise.carrotproject.domain.post.entity.Post;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MtPlaceDto {
    private Long mtPlaceId;
    private Post post;
    private Double lat;
    private Double lon;
    private String placeInfo;
}
