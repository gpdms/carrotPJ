package com.exercise.carrotproject.domain.converter;


import com.exercise.carrotproject.domain.enumlist.Loc;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class LocConverter implements AttributeConverter<Loc, String> {

    @Override
    public String convertToDatabaseColumn(Loc loc) {
        if(loc == null) {
            return null;
        }
        return loc.getCode();
    }

    @Override
    public Loc convertToEntityAttribute(String dbData) {
        if(dbData == null) {
            return null;
        }
        return Stream.of(Loc.values())
                .filter(l -> l.getCode().equals(dbData))
                .findFirst()
                .orElse(null);
    }
}
