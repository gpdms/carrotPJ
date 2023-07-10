package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.member.util.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.post.dto.PostDto;

import java.util.List;
import java.util.stream.Collectors;


public class PostEntityDtoMapper {

    //Dto -> Entity 변환
    public static Post dtoToEntity(PostDto postDto) {
        return Post.builder()
                .postId(postDto.getPostId())
                .title(postDto.getTitle())
                .member(MemberEntityDtoMapper.toEntity(postDto.getMember()))
                .price(postDto.getPrice())
                .content(postDto.getContent())
                .loc(postDto.getLoc())
                .category(postDto.getCategory())
                .hideState(postDto.getHideState())
                .sellState(postDto.getSellState())
                .hits(postDto.getHits())
                .build();
    }

    //Entity->Dto 변환
    public static PostDto entityToDto (Post post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .member(MemberEntityDtoMapper.toDto(post.getMember()))
                .price(post.getPrice())
                .content(post.getContent())
                .loc(post.getLoc())
                .category(post.getCategory())
                .hideState(post.getHideState())
                .sellState(post.getSellState())
                .hits(post.getHits())
                .createdTime(post.getCalculatedTimeForPost())
                .build();
    }

    //Entity리스트->Dto리스트
    public static List<PostDto> toDtoList(List<Post> postEntityList){
        return postEntityList.stream().map(PostEntityDtoMapper::entityToDto).collect(Collectors.toList());
    }


}
