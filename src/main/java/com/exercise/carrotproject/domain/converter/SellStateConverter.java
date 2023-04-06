package com.exercise.carrotproject.domain.converter;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.SellState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class SellStateConverter implements AttributeConverter<SellState, String> {

    //Enum->DB
    @Override
    public String convertToDatabaseColumn(SellState sellState) {
        if (sellState == null){
            return null;
        }
        return sellState.getSellStateCode();
    }

    //DB->Enum
    @Override
    public SellState convertToEntityAttribute(String sellState) {
        if(sellState == null){
            return null;
        }
        return Stream.of(SellState.values())
                .filter(m->m.getSellStateCode().equals(sellState))
                .findFirst()
                .orElse(null);
    }

}
