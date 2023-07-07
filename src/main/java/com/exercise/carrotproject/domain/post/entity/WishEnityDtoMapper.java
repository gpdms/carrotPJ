package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.member.util.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.post.dto.WishDto;

import java.util.List;
import java.util.stream.Collectors;

public class WishEnityDtoMapper {
    //Dto -> Entity 변환
    public static Wish dtoToEntity(WishDto wishDto){
        return Wish.builder()
                .post(PostEntityDtoMapper.dtoToEntity(wishDto.getPost()))
                .member(MemberEntityDtoMapper.toMemberEntity(wishDto.getMember()))
                .build();

    }

    //Entity->Dto 변환
    public static WishDto entityToDto (Wish wish){
        return WishDto.builder()
                .post(PostEntityDtoMapper.entityToDto(wish.getPost()))
                .member(MemberEntityDtoMapper.toMemberDto(wish.getMember()))
                .build();
    }

    //Entity리스트->Dto리스트
    public static List<WishDto> toDtoList(List<Wish> wishEntityList){
        return wishEntityList.stream().map(WishEnityDtoMapper::entityToDto).collect(Collectors.toList());
    }

}
