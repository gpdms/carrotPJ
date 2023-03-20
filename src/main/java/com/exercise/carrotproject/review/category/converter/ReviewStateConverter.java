package com.exercise.carrotproject.review.category.converter;



import com.exercise.carrotproject.review.category.ReviewState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class ReviewStateConverter implements AttributeConverter<ReviewState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ReviewState reviewState) {
        if(reviewState == null) {
            return null;
        }
        return reviewState.getStateCode();
    }

    @Override
    public ReviewState convertToEntityAttribute(Integer dbData) {
        if(dbData == null) {
            return null;
        }
        return Stream.of(ReviewState.values())
                .filter(rs -> rs.getStateCode().equals(dbData) )
                .findFirst()
                .orElse(null);
    }

}

