package com.exercise.carrotproject.domain.converter;



import com.exercise.carrotproject.domain.enumList.ReviewState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class ReviewStateConverter implements AttributeConverter<ReviewState, String> {

    @Override
    public String convertToDatabaseColumn(ReviewState reviewState) {
        if(reviewState == null) {
            return null;
        }
        return reviewState.getStateCode();
    }

    @Override
    public ReviewState convertToEntityAttribute(String dbData) {
        if(dbData == null) {
            return null;
        }
        return Stream.of(ReviewState.values())
                .filter(rs -> rs.getStateCode().equals(dbData) )
                .findFirst()
                .orElse(null);
    }

}

