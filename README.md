# 망고마켓 (Mango Project)



## 🍋 프로젝트 소개

> http://15.164.25.45:8888/


 &nbsp;&nbsp; http://3.37.98.151:8888/

* 중고거래 플랫폼
    * 중고거래 사이트에 필요한 기능이 포집되어있는 기존 플랫폼(당근마켓)을 참고하여 제작하였습니다.
    * 당근마켓은 앱 위주의 플랫폼으로 웹페이지가 부실하여, 앱의 기능을 웹에서 구현하고자하였습니다.


## 프로젝트 기간

* 2023.03.13 ~ 2023.05.08


## 팀원 소개

* 박재경 : 회원, 리뷰, 검색
* 최혜은 : 상품 게시글, 거래 내역, 찜
* 남호용 : 채팅

## 기술 스택

* Frontend : Thymeleaf, Bootstrap
* Backend 
  * Java11
  * Framework : Spring Boot (2.7.2)
  * ORM : JPA, QueryDsl
  * Database : H2, MariaDB (10.6.10)
  * IDE : IntelliJ
  * Distribution : Amazon EC2, Amazon RDS

## 주요 기능

#### &nbsp;회원
1. 로그인/카카오톡 로그인, 회원가입, 비밀번호 재발급
2. 회원정보
    * 회원정보 수정(프로필/비밀번호)
    * 판매내역/구매내역/찜
3. 회원등급(매너온도)
4. 차단

#### &nbsp;상품 게시판
1. 게시글(글,사진,지도) 업로드 / 수정 / 삭제
2. 판매 상태 변경(예약중, 판매중, 판매완료)
3. 게시판
4. 게시글 상세보기
5. 조회수
6. 검색 
#### &nbsp;채팅
1. 텍스트 전송
2. 이미지 전송
3. 실시간 채팅
4. 실시간 알림
5. 채팅 목록 보기(나의 채팅 목록, 상품 게시글의 채팅)

#### &nbsp;리뷰
1. 리뷰 작성 / 삭제
2. 리뷰 지표 합산

---
## 화면


<table>
    <tr>
        <td align="center" colspan="2">메인 페이지</td>
    </tr>
    <tr>
        <td colspan="2" align="center">
            <img width="47%" alt="main" src="https://github.com/gpdms/carrotPJ/assets/109894921/43222af0-41ce-423c-b20d-1385dde0d5e8">
           <img width="47%" alt="main2" src="https://github.com/gpdms/carrotPJ/assets/109894921/85cb0d61-d7da-4e7f-91d3-77a96fa4c013">
        </td>
    </tr>
    <tr>
        <td align="center">로그인/회원가입</td>
        <td align="center">멤버 홈 </td>
    </tr>
    <tr>
        <td width="50%">
            <img width="49%" alt="login" src="https://github.com/gpdms/carrotPJ/assets/109894921/0478c981-a9bc-46da-a543-623a8489acb1">
            <img width="49%" alt="signup" src="https://github.com/gpdms/carrotPJ/assets/109894921/77ac841d-e8ef-4e5a-9152-a3eda0fb6eff">
        </td>
        <td width="50%">
           <img width="49%" alt="myhome" src="https://github.com/gpdms/carrotPJ/assets/109894921/2ca8e42a-398e-4e22-ba67-3072b4d21d53">
            <img width="49%" alt="myhome2" src="https://github.com/gpdms/carrotPJ/assets/109894921/3db8e36f-3279-46b0-bea7-cbef85486e52">
        </td>
    </tr>
    <tr>
        <td align="center">회원정보(프로필/비밀번호 변경)</td>
        <td align="center">회원정보(<strong>판매</strong>/구매/찜목록)</td>
    </tr>
    <tr>
        <td>
           <img width="100%" alt="pfEdit" src="https://github.com/gpdms/carrotPJ/assets/109894921/2d1c5fe5-04c7-44b3-9fda-45521203640d">
        </td>
        <td>
           <img width="49%" alt="sellList" src="https://github.com/gpdms/carrotPJ/assets/109894921/3459f2d7-e81f-4cbc-897a-3b4082375bb7">
           <img width="49%" alt="sellList2" src="https://github.com/gpdms/carrotPJ/assets/109894921/d0556bd0-73c5-4ed4-8fc8-6296333136fd">
       </td>
    </tr>
    <tr>
        <td align="center">게시판</td>
        <td align="center">카테고리/검색</td>
    </tr>
    <tr>
        <td>
            <img width="100%" alt="image" src="https://github.com/gpdms/carrotPJ/assets/109894921/c216c37b-0002-484e-b9c4-603c7c686df2">
        </td>
        <td>
            <img width="100%" alt="category" src="https://github.com/gpdms/carrotPJ/assets/109894921/580b9c36-ebdc-4d25-99b3-6c2d28b7fd38">
       </td>
    </tr>
    <tr>
        <td align="center">게시글 작성/수정</td>
        <td align="center">게시글 조회</td>
    </tr>
    <tr>
        <td>
           <img width="100%" alt="postUpload" src="https://github.com/gpdms/carrotPJ/assets/109894921/90148052-0869-465e-9a8b-8a2a090ff254">
         </td>
        <td>
           <img width="100%" alt="postDetail" src="https://github.com/gpdms/carrotPJ/assets/109894921/7a28574e-dc3e-446d-8e4b-8b9306e59600">
       </td>
    </tr>
    <tr>
        <td align="center">리뷰 작성/조회</td>
        <td align="center">리뷰 통계/메시지</td>
    </tr>
    <tr>
        <td>
           <img width="49%" alt="reviewUpload" src="https://github.com/gpdms/carrotPJ/assets/109894921/8209569a-3824-46b2-8274-7752b1bff3a2">
            <img width="49%" alt="reviewDetail" src="https://github.com/gpdms/carrotPJ/assets/109894921/732d38fa-8fe6-4275-8504-dcf983c966e0">
       </td>
        <td>
           <img width="49%" alt="reviewList2" src="https://github.com/gpdms/carrotPJ/assets/109894921/c73d3812-16b9-49a4-8474-ee8c697d6c48">
            <img width="49%" alt="mannerDetail" src="https://github.com/gpdms/carrotPJ/assets/109894921/0ac6442b-e378-4e62-9f76-737cf8676b55">
       </td>
    </tr>
    <tr>
        <td align="center">채팅</td>
        <td align="center">채팅 목록</td>
    </tr>
    <tr>
        <td>
       <img width="100%" alt="chat" src="https://github.com/gpdms/carrotPJ/assets/109894921/c241277b-64e5-4687-935b-650c5fa9b291">
       </td>
        <td>
       <img width="100%" alt="chatRoom1" src="https://github.com/gpdms/carrotPJ/assets/109894921/a6f85404-2012-4a10-8818-930eaded4f64">
      <img width="100%" alt="buyerSelection" src="https://github.com/gpdms/carrotPJ/assets/109894921/1a886293-ee32-4ac4-ac86-03e288941d81">
       </td>
    </tr>
</table>






## ERD
<img width="100%" alt="ERD" src="https://github.com/gpdms/carrotPJ/assets/118142992/ee5238ae-110f-4fc3-8cb3-79ebc48f7e9a">




## 소스코드 바로가기

* 회원
  * [DTO,Entity,Repository,Service](https://github.com/gpdms/carrotPJ/tree/main/src/main/java/com/exercise/carrotproject/domain/member)
  *  [Controller](https://github.com/gpdms/carrotPJ/tree/main/src/main/java/com/exercise/carrotproject/web/member/controller)

* 게시글
   * [DTO,Entity,Repository,Service](https://github.com/gpdms/carrotPJ/tree/main/src/main/java/com/exercise/carrotproject/domain/post)
   * [Controller](https://github.com/gpdms/carrotPJ/tree/main/src/main/java/com/exercise/carrotproject/web/post/controller)
   
* 채팅
   * [DTO,Entity,Repository,Service](https://github.com/gpdms/carrotPJ/tree/main/src/main/java/com/exercise/carrotproject/domain/chat)
   * [Controller](https://github.com/gpdms/carrotPJ/tree/main/src/main/java/com/exercise/carrotproject/web/chat/controller)
   
* 리뷰
   * [DTO,Entity,Repository,Service](https://github.com/gpdms/carrotPJ/tree/main/src/main/java/com/exercise/carrotproject/domain/review)
   * [Controller](https://github.com/gpdms/carrotPJ/tree/main/src/main/java/com/exercise/carrotproject/web/review/controller)

* HTML
   * [Layout,nav바,홈,회원,게시글,채팅 HTML](https://github.com/gpdms/carrotPJ/tree/NewNewHyeeun/src/main/resources/templates)

* CSS
   * [Bootstrap,직접제작CSS](https://github.com/gpdms/carrotPJ/tree/NewNewHyeeun/src/main/resources/static/css)
