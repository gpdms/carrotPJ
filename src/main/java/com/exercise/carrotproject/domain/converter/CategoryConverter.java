package com.exercise.carrotproject.domain.converter;

import com.exercise.carrotproject.domain.common.enumList.Category;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class CategoryConverter implements AttributeConverter<Category,Integer> {

    //Enum->DB
    @Override
    public Integer convertToDatabaseColumn(Category category) {
        if (category == null){
            return null;
        }
        return category.getCategoryCode();
    }

    //DB->Enum
    @Override
    public Category convertToEntityAttribute(Integer category) {
        if(category == null){
            return null;
        }
        return Stream.of(Category.values())
                .filter(m->m.getCategoryCode().equals(category))
                .findFirst()
                .orElse(null);
    }
}
