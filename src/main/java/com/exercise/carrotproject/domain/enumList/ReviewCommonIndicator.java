package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@AllArgsConstructor
@Getter
public enum ReviewCommonIndicator { //only 조회용 indicator
    N1("시간약속을 안 지켜요."),
    N2("채팅 메시지를 읽고도 답이 없어요."),
    N3("원하지 않는 가격을 계속 요구해요."),
    N4("예약만하고 거래시간을 명확하게 알려주지 않아요."),
    N5("거래 시간과 장소를 정한후 연락이 안돼요."),
    N6("거래 시간과 장소를 정한 후 거래 직전 취소했어요."),
    N7("약속장소에 나타나지 않았어요."),
    N8("반말을 사용해요."),
    N9("불친절해요."),

    P1("응답이 빨라요."),
    P2("친절하고 매너가 좋아요."),
    P3("시간 약속을 잘 지켜요.");

    private final String description;



}
