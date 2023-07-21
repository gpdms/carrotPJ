package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ReviewSellerIndicator {
    N1("시간약속을 안 지켜요.", getNScore()),
    N2("채팅 메시지를 읽고도 답이 없어요.", getNScore()),
    N3("원하지 않는 가격을 계속 요구해요.", getNScore()),
    N4("예약만하고 거래시간을 명확하게 알려주지 않아요.", getNScore()),
    N5( "거래 시간과 장소를 정한후 연락이 안돼요.", getNScore()),
    N6( "거래 시간과 장소를 정한 후 거래 직전 취소했어요.", getNScore()),
    N7( "약속장소에 나타나지 않았어요.", getNScore()),
    N8( "반말을 사용해요.", getNScore()),
    N9( "불친절해요.", getNScore()), //공통
    NS1("상품상태가 설명과 달라요.", getNScore()),

    P1("응답이 빨라요.", getPScore()),
    P2("친절하고 매너가 좋아요.", getPScore()),
    P3("시간 약속을 잘 지켜요.", getPScore()),//공통
    PS1("상품 상태가 설명한 것과 같아요.", getPScore()),
    PS2("좋은 상품을 저렴하게 판매해요.", getPScore()),
    PS3("상품 설명이 자세해요.", getPScore()),
    PS4("나눔을 해주셨어요.", getPScore());

    private final String description;
    private double score;

    private static double getNScore(){
        return -(5000/10);
    }
    private static double getPScore () {
        return 5000/7;
    }

    public static List<ReviewSellerIndicator> findAllByEnumName(List<String> searchNames){
        return searchNames.stream()
                .map(ReviewSellerIndicator::valueOf)
                .collect(Collectors.toList());
    }

   public static double sumScore(List<ReviewSellerIndicator> indicatorList) {
       return indicatorList.stream()
               .map(value -> value.getScore())
               .mapToDouble(Double::doubleValue)
               .sum();
   }

}
