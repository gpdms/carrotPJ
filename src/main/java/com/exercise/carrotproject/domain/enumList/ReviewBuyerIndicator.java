package com.exercise.carrotproject.domain.enumList;

import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@AllArgsConstructor
@Getter
public enum ReviewBuyerIndicator {
    N1("시간약속을 안 지켜요.", getNScore()),
    N2("채팅 메시지를 읽고도 답이 없어요.", getNScore()),
    N3("원하지 않는 가격을 계속 요구해요.", getNScore()),
    N4("예약만하고 거래시간을 명확하게 알려주지 않아요.", getNScore()),
    N5("거래 시간과 장소를 정한후 연락이 안돼요.", getNScore()),
    N6("거래 시간과 장소를 정한 후 거래 직전 취소했어요.", getNScore()),
    N7("약속장소에 나타나지 않았어요.", getNScore()),
    N8("반말을 사용해요.", getNScore()),
    N9("불친절해요.", getNScore()), //공통 지표
    NB1("단순 변심으로 환불을 요구해요.", getNScore()),

    P1("응답이 빨라요.", getPScore()),
    P2("친절하고 매너가 좋아요.", getPScore()),
    P3("시간 약속을 잘 지켜요.", getPScore()),//공통 지표
    PB1("제가 있는 곳까지 와서 거래했어요.",getPScore());

    private final String description;
    private final double score;

    private final static int STANDARD_TOTAL_SCORE = 5000;
    private final static int POSITIVE_INDICATOR_COUNT = 4;
    private final static int NEGATIVE_INDICATOR_COUNT = 10;

    private static double getPScore () {
        return STANDARD_TOTAL_SCORE/POSITIVE_INDICATOR_COUNT;
    }
    private static double getNScore(){
        return -(STANDARD_TOTAL_SCORE/NEGATIVE_INDICATOR_COUNT);
    }

    public static List<ReviewBuyerIndicator> findAllByEnumName(List<String> searchNames){
        return searchNames.stream()
                .map(ReviewBuyerIndicator::valueOf)
                .collect(Collectors.toList());
    }

    public static double sumScore(List<ReviewBuyerIndicator> indicatorList) {
        return indicatorList.stream()
                .map(value -> value.getScore())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

}
