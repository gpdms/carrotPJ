package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ReviewBuyerIndicator {
    NB1("시간약속을 안 지켜요.", -1.0),
    NB2("채팅 메시지를 읽고도 답이 없어요.", -1.0),
    NB3("원하지 않는 가격을 계속 요구해요.", -1.0),
    NB4("예약만하고 거래시간을 명확하게 알려주지 않아요.", -1.0),
    NB5("거래 시간과 장소를 정한후 연락이 안돼요.", -1.0),
    NB6("거래 시간과 장소를 정한 후 거래 직전 취소했어요.", -1.0),
    NB7("약속장소에 나타나지 않았어요.", -1.0),
    NB8("반말을 사용해요.", -1.0),
    NB9("불친절해요.", -1.0),
    NB10("단순 변심으로 환불을 요구해요.", -1.0),

    PB1("응답이 빨라요.", 2.5),
    PB2("친절하고 매너가 좋아요.", 2.5),
    PB3("시간 약속을 잘 지켜요.", 2.5),
    PB4("제가 있는 곳까지 와서 거래했어요.",2.5);

    private final String description;
    private final Double score;
}
