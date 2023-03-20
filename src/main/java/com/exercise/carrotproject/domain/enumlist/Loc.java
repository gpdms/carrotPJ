package com.exercise.carrotproject.domain.enumlist;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Loc {
 GANGDONG("L1","강동"),
 GANGSEO("L2","강서"),
 GANGNAM("L3","강남"),
 GANGBUK("L4","강북");

 private final String code;
 private final String name;
}
