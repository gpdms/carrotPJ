package com.exercise.carrotproject.domain.enumList;

import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ReviewSellerIndicator {
    N1("시간약속을 안 지켜요.", -1.0),
    N2("채팅 메시지를 읽고도 답이 없어요.", -1.0),
    N3("원하지 않는 가격을 계속 요구해요.", -1.0),
    N4("예약만하고 거래시간을 명확하게 알려주지 않아요.", -1.0),
    N5( "거래 시간과 장소를 정한후 연락이 안돼요.", -1.0),
    N6( "거래 시간과 장소를 정한 후 거래 직전 취소했어요.", -1.0),
    N7( "약속장소에 나타나지 않았어요.", -1.0),
    N8( "반말을 사용해요.", -1.0),
    N9( "불친절해요.", -1.0), //공통

    NS1("상품상태가 설명과 달라요.", -1.0), //따로

    P1("응답이 빨라요.", 1.4),
    P2("친절하고 매너가 좋아요.", 1.4),
    P3("시간 약속을 잘 지켜요.", 1.4), //공통

    PS1("상품 상태가 설명한 것과 같아요.", 1.4),
    PS2("좋은 상품을 저렴하게 판매해요.", 1.4),
    PS3("상품 설명이 자세해요.", 1.4),
    PS4("나눔을 해주셨어요.", 1.4); //따로

    private final String description;
    private final double score;

    public static List<ReviewSellerIndicator> findAllByEnumName(List<String> searchCodes){
        return searchCodes.stream()
                .map(ReviewSellerIndicator::findByEnumName)
                .collect(Collectors.toList());
        //= return searchCodes.stream()
        //            .map(code -> Arrays.stream(ReviewSellerIndicator.values())
        //                    .filter(indicator -> indicator.name().equals(code))
        //                    .findFirst()
        //                    .orElse(null))
        //            .collect(Collectors.toList());
    }
    public static ReviewSellerIndicator findByEnumName(String enumName){
        return Arrays.stream(values())
                .filter(value -> value.name().equals(enumName))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Invalid SellerIndicator Name"));
    }

   public static double sumScore(List<ReviewSellerIndicator> indicatorList) {
       double sum = indicatorList.stream()
               .map(value -> value.getScore())
               .mapToDouble(Double::doubleValue)
               .sum();
       return (double)Math.round(sum*1000)/1000;
   }

}
