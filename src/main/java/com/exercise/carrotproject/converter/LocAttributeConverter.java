package com.exercise.carrotproject.converter;

import com.exercise.carrotproject.enumlist.Loc;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LocAttributeConverter implements AttributeConverter<Loc, String> {

    @Override
    public String convertToDatabaseColumn(Loc attribute) {
        return attribute.getLocCode();
    }

    @Override
    public Loc convertToEntityAttribute(String dbData) {
        return Loc.getLocNameByCode(dbData);
    }
}
