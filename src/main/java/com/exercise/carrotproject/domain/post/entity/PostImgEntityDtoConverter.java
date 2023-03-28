package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.post.dto.PostImgDto;

import java.util.List;
import java.util.stream.Collectors;

public class PostImgEntityDtoConverter {

    //Dto -> Entity 변환
    public static PostImg dtoToEntity(PostImgDto postImgDto){
        return PostImg.builder()
                .imgId(postImgDto.getImgId())
                .post(postImgDto.getPost())
                .orgName(postImgDto.getOrgName())
                .savedName(postImgDto.getSavedName())
                .savedPath(postImgDto.getSavedPath())
                .build();
    }

    //Entity->Dto 변환
    public static PostImgDto entityToDto (PostImg postImg){
        return PostImgDto.builder()
                .imgId(postImg.getImgId())
                .post(postImg.getPost())
                .orgName(postImg.getOrgName())
                .savedName(postImg.getSavedName())
                .savedPath(postImg.getSavedPath())
                .build();
    }


    //Entity리스트->Dto리스트
    public static List<PostImgDto> toDtoList(List<PostImg> entityList){
        return entityList.stream().map(PostImgEntityDtoConverter::entityToDto).collect(Collectors.toList());
    }

}
