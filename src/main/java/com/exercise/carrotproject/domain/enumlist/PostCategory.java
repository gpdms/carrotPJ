package com.exercise.carrotproject.domain.enumlist;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostCategory {
    DIGITAL_DEVICE("디지털기기"),
    HOME_ELECTRONICS("가전"),
    FURNITURE("가구/인테리어"),
    HOUSEHOLD_GOODS ("생활용품"),
    FOOD ("식품"),
    FASHION ("패션"),
    BEAUTY("뷰티/미용"),
    BOOK ("도서"),
    VOUCHER("티켓/교환권"),
    HOBBY("취미/게임/음반"),
    SPORTS("스포츠/레저"),
    ETC("기타 중고물품");

    private final String name;
}
