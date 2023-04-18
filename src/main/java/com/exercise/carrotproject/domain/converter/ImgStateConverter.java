package com.exercise.carrotproject.domain.converter;

import com.exercise.carrotproject.domain.enumList.ImgState;
import com.exercise.carrotproject.domain.enumList.ReadState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class ImgStateConverter implements AttributeConverter<ImgState, String> {

    @Override
    public String convertToDatabaseColumn(ImgState attribute) {
        return attribute.getImgStateCode();
    }

    @Override
    public ImgState convertToEntityAttribute(String dbData) {
        return Stream.of(ImgState.values())
                .filter(rs -> rs.getImgStateCode().equals(dbData))
                .findFirst()
                .orElseThrow();
    }
}
