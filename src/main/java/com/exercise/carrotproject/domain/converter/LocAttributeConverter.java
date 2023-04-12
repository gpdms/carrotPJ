package com.exercise.carrotproject.domain.converter;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.ReadState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class LocAttributeConverter implements AttributeConverter<Loc, String> {

    @Override
    public String convertToDatabaseColumn(Loc attribute) {
        return attribute.getLocCode();
    }

    @Override
    public Loc convertToEntityAttribute(String dbData) {
        return Stream.of(Loc.values())
                .filter(rs -> rs.getLocCode().equals(dbData))
                .findFirst()
                .orElseThrow();
    }
}
