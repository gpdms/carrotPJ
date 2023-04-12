package com.exercise.carrotproject.domain.converter;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.ReadState;
import com.exercise.carrotproject.domain.enumList.ReviewState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class ReadStateConverter implements AttributeConverter<ReadState, String> {

    @Override
    public String convertToDatabaseColumn(ReadState attribute) {
        return attribute.getReadStateCode();
    }

    @Override
    public ReadState convertToEntityAttribute(String dbData) {
        return Stream.of(ReadState.values())
                .filter(rs -> rs.getReadStateCode().equals(dbData))
                .findFirst()
                .orElseThrow();
    }
}
