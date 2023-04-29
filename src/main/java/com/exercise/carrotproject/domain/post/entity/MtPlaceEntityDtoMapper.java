package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.post.dto.MtPlaceDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;

public class MtPlaceEntityDtoMapper {
    //Dto -> Entity 변환
    public static MtPlace dtoToEntity(MtPlaceDto mtPlaceDto){
        return MtPlace.builder()
                .mtPlaceId(mtPlaceDto.getMtPlaceId())
                .post(PostEntityDtoMapper.dtoToEntity(mtPlaceDto.getPost()))
                .lat(mtPlaceDto.getLat())
                .lon(mtPlaceDto.getLon())
                .placeInfo(mtPlaceDto.getPlaceInfo())
                .build();

    }

    //Entity->Dto 변환
    public static MtPlaceDto entityToDto (MtPlace mtPlace){
        return MtPlaceDto.builder()
                .mtPlaceId(mtPlace.getMtPlaceId())
                .post(PostEntityDtoMapper.entityToDto(mtPlace.getPost()))
                .lat(mtPlace.getLat())
                .lon(mtPlace.getLon())
                .placeInfo(mtPlace.getPlaceInfo())
                .build();
    }
}
