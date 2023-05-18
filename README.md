# 망고마켓 (Mango Project)



## 🍋 프로젝트 소개

> http://15.164.25.45:8888/

* 중고거래 플랫폼  
  * 중고거래 사이트에 필요한 기능이 포집되어있는 기존 플랫폼(당근마켓)을 참고하여 제작하였습니다. 
  * 당근마켓은 앱 위주의 플랫폼으로 웹페이지가 부실하여, 앱의 기능을 웹에서 구현하고자하였습니다. 


## 프로젝트 기간

* 2023.03.13 ~ 2023.05.08


## 팀원 소개

* 박재경 : 회원, 리뷰
* 최혜은 : 상품 게시글
* 남호용 : 채팅

## 기술 스택

* Frontend : Thymeleaf, Bootstrap
* Backend : 
  * Java11
  * Framework : Spring Boot2.7.2
  * ORM : JPA, QueryDsl
  * Database : H2, MariaDB 10.6.10
  * IDE : IntelliJ
  * Distribution : Amazon EC2, Amazon RDS

## 주요 기능

#### &nbsp;회원
1. 로그인/카카오톡 로그인, 회원가입, 비밀번호 재발급
2. 회원정보
   * 회원정보 수정(프로필/비밀번호)
   * 판매내역/구매내역/찜 [최혜은]
3. 회원등급(매너온도)
4. 차단

#### &nbsp;상품 게시판
1. 게시글(글,사진,지도) 업로드 / 수정 / 삭제
2. 판매 상태 변경(예약중, 판매중, 판매완료)
3. 게시판
4. 게시글 상세보기
5. 조회수
6. 검색 [박재경]

#### &nbsp;채팅
1. 텍스트 전송 
2. 이미지 전송 
3. 실시간 채팅
4. 실시간 알림
5. 채팅 목록 보기(나의 채팅 목록, 상품 게시글의 채팅)

#### &nbsp;리뷰
1. 리뷰 작성 / 삭제
2. 리뷰 지표 합산 


## 화면


<table>
    <tr>
        <td align="center" colspan="2">메인 페이지</td>
    </tr>
    <tr>
        <td colspan="2">
            <img width="100%" alt="image" src="https://github.com/gpdms/carrotPJ/assets/109894921/5f7dbde1-13d9-4b67-8ba8-227b60d32b7e">
        </td>
    </tr>
    <tr>
        <td align="center">로그인/회원가입</td>
        <td align="center">멤버 홈 </td>
    </tr>
    <tr>
        <td>
            <img width="49%" alt="스크린샷 2023-05-18 오후 7 47 31" src="https://github.com/gpdms/carrotPJ/assets/109894921/0478c981-a9bc-46da-a543-623a8489acb1">
            <img width="49%" alt="회원가입" src="https://github.com/gpdms/carrotPJ/assets/109894921/77ac841d-e8ef-4e5a-9152-a3eda0fb6eff">
        </td>
        <td>
        </td>
    </tr>
    <tr>
        <td align="center">회원정보(프로필/비밀번호 변경)</td>
        <td align="center">회원정보(<strong>판매</strong>/구매/찜목록)</td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td align="center">게시판</td>
        <td align="center">카테고리/검색</td>
    </tr>
    <tr>
        <td>
            <img width="100%" alt="image" src="https://github.com/gpdms/carrotPJ/assets/109894921/c216c37b-0002-484e-b9c4-603c7c686df2">
        </td>
        <td></td>
    </tr>
    <tr>
         <td align="center">게시글 작성/수정</td>
        <td align="center">게시글 조회</td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td align="center">리뷰 작성/조회</td>
        <td align="center">리뷰 통계/메시지</td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td align="center">채팅</td>
        <td align="center">채팅 목록</td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
</table>

