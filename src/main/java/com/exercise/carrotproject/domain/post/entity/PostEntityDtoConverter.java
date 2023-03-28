package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.member.MemberRepository;
import com.exercise.carrotproject.domain.post.dto.PostDto;

import java.util.List;
import java.util.stream.Collectors;


public class PostEntityDtoConverter {
    private static MemberRepository memberRepository;

    //Dto -> Entity 변환
    public static Post dtoToEntity(PostDto postDto) {
        return Post.builder()
                .postId(postDto.getPostId())
                .title(postDto.getTitle())
                .member(memberRepository.findById(postDto.getMemberId()).orElse(null))
                .price(postDto.getPrice())
                .content(postDto.getContent())
                .loc(postDto.getLoc())
                .category(postDto.getCategory())
                .hideState(postDto.getHideState())
                .sellState(postDto.getSellState())
                .hits(postDto.getHits())
                .wishPlace(postDto.getWishPlace())
                .build();
    }

    //Entity->Dto 변환
    public static PostDto entityToDto (Post post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .memberId(post.getMember().getMemId())
                .price(post.getPrice())
                .content(post.getContent())
                .loc(post.getLoc())
                .category(post.getCategory())
                .hideState(post.getHideState())
                .sellState(post.getSellState())
                .hits(post.getHits())
                .wishPlace(post.getWishPlace())
                .build();
    }

    //Entity리스트->Dto리스트
    public static List<PostDto> toDtoList(List<Post> postEntityList){
        return postEntityList.stream().map(PostEntityDtoConverter::entityToDto).collect(Collectors.toList());
    }


}
