package com.exercise.carrotproject.domain.common.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewSellerIndicator {
    NS1("시간약속을 안 지켜요.", -1.0),
    NS2("채팅 메시지를 읽고도 답이 없어요.", -1.0),
    NS3("원하지 않는 가격을 계속 요구해요.", -1.0),
    NS4("예약만하고 거래시간을 명확하게 알려주지 않아요.", -1.0),
    NS5( "거래 시간과 장소를 정한후 연락이 안돼요.", -1.0),
    NS6( "거래 시간과 장소를 정한 후 거래 직전 취소했어요.", -1.0),
    NS7( "약속장소에 나타나지 않았어요.", -1.0),
    NS8( "반말을 사용해요.", -1.0),
    NS9( "불친절해요.", -1.0),
    NS10("상품상태가 설명과 달라요.", -1.0),

    PS1("응답이 빨라요.", 1.4),
    PS2("친절하고 매너가 좋아요.", 1.4),
    PS3("시간 약속을 잘 지켜요.", 1.4),
    PS4("상품 상태가 설명한 것과 같아요.", 1.4),
    PS5("좋은 상품을 저렴하게 판매해요.", 1.4),
    PS6("상품 설명이 자세해요.", 1.4),
    PS7("나눔을 해주셨어요.", 1.4);

    private final String description;
    private final Double score;

}
