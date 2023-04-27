package com.exercise.carrotproject.domain.post.dto;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class WishDto {
    private Long wishId;
    private PostDto post;
    private MemberDto member;
}
