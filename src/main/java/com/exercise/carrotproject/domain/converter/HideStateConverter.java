package com.exercise.carrotproject.domain.converter;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.HideState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class HideStateConverter implements AttributeConverter<HideState, String> {

    //Enum->DB
    @Override
    public String convertToDatabaseColumn(HideState hideState) {
        if (hideState == null){
            return null;
        }
        return hideState.getHideStateCode();
    }

    //DB->Enum
    @Override
    public HideState convertToEntityAttribute(String hideState) {
        if(hideState == null){
            return null;
        }
        return Stream.of(HideState.values())
                .filter(m->m.getHideStateCode().equals(hideState))
                .findFirst()
                .orElse(null);
    }
}
