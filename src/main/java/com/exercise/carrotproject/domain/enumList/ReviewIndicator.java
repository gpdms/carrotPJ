package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum ReviewIndicator {
    N1("시간약속을 안 지켜요."),
    N2("채팅 메시지를 읽고도 답이 없어요."),
    N3("원하지 않는 가격을 계속 요구해요."),
    N4("예약만하고 거래시간을 명확하게 알려주지 않아요."),
    N5("거래 시간과 장소를 정한후 연락이 안돼요."),
    N6("거래 시간과 장소를 정한 후 거래 직전 취소했어요."),
    N7("약속장소에 나타나지 않았어요."),
    N8("반말을 사용해요."),
    N9("불친절해요."),
    NB1("단순 변심으로 환불을 요구해요."),
    NS1("상품상태가 설명과 달라요."),

    P1("응답이 빨라요."),
    P2("친절하고 매너가 좋아요."),
    P3("시간 약속을 잘 지켜요."),
    PB1("제가 있는 곳까지 와서 거래했어요."),
    PS1("상품 상태가 설명한 것과 같아요."),
    PS2("좋은 상품을 저렴하게 판매해요."),
    PS3("상품 설명이 자세해요."),
    PS4("나눔을 해주셨어요.");

    private final String description;

}
