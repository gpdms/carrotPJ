package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum Category {
    DIGITAL_DEVICE(1, "디지털기기"),
    HOME_ELECTRONICS(2,"가전"),
    FURNITURE(3,"가구/인테리어"),
    HOUSEHOLD_GOODS(4, "생활용품"),
    FOOD(5, "식품"),
    FASHION(6, "패션"),
    BEAUTY(7, "뷰티/미용"),
    BOOK(8, "도서"),
    VOUCHER(9, "티켓/교환권"),
    HOBBY(10, "취미/게임/음반"),
    SPORTS(11, "스포츠/레저"),
    ETC(12, "기타 중고물품");

    //필드
    Integer categoryCode;
    String categoryName;
    //생성자
    //Getter



}