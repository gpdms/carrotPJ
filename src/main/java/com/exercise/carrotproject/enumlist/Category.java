package com.exercise.carrotproject.enumlist;

public enum Category {
    FOOD(1, "식품"),
    COSMETICS(2,"화장품"),
    FURNITURE(3,"가구"),
    CLOTHING(4,"의류"),
    SHOES(5,"신발"),
    BOOKS(6,"도서");

    //필드
    int categoryCode;
    String categoryName;

    //생성자
    Category(int categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }

    //Getter
    public int getCategoryCode() {
        return categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
