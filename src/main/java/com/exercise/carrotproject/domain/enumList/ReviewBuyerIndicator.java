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
    N1("시간약속을 안 지켜요.", -1.0),
    N2("채팅 메시지를 읽고도 답이 없어요.", -1.0),
    N3("원하지 않는 가격을 계속 요구해요.", -1.0),
    N4("예약만하고 거래시간을 명확하게 알려주지 않아요.", -1.0),
    N5("거래 시간과 장소를 정한후 연락이 안돼요.", -1.0),
    N6("거래 시간과 장소를 정한 후 거래 직전 취소했어요.", -1.0),
    N7("약속장소에 나타나지 않았어요.", -1.0),
    N8("반말을 사용해요.", -1.0),
    N9("불친절해요.", -1.0), //공통

    NB1("단순 변심으로 환불을 요구해요.", -1.0), //따로

    P1("응답이 빨라요.", 2.5),
    P2("친절하고 매너가 좋아요.", 2.5),
    P3("시간 약속을 잘 지켜요.", 2.5),

    PB1("제가 있는 곳까지 와서 거래했어요.",2.5); //따로 집계

    private final String description;
    private final double score;

    public static List<ReviewBuyerIndicator> findAllByEnumName(List<String> searchCodes){
        return searchCodes.stream()
                .map(ReviewBuyerIndicator::findByEnumName)
                .collect(Collectors.toList());
    }
    public static ReviewBuyerIndicator findByEnumName(String enumName){
        return Arrays.stream(values())
                .filter(value -> value.name().equals(enumName))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Invalid buyerIndicator Name"));
    }
    public static double sumScore(List<ReviewBuyerIndicator> indicatorList) {
        double sum = indicatorList.stream()
                .map(value -> value.getScore())
                .mapToDouble(Double::doubleValue)
                .sum();
        System.out.println("합계----------------------------2"+Math.round(sum*1000)/1000);
        return Math.round(sum*1000)/1000;
    }

}
