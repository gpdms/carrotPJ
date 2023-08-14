package com.exercise.carrotproject.web.review.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CursorResult<T> {
    private List<T> valueList;
    private boolean hasNext;
    private long totalElements;
}