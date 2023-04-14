package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.domain.chat.dto.ChatRoomDto;
import com.exercise.carrotproject.domain.post.dto.MtPlaceDto;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import com.exercise.carrotproject.domain.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;


    @Value("${default.postImg}")
    private String defaultPostImg;





    @GetMapping("/board")
    public String home(){
        return "redirect:/post/board";
    }

    @GetMapping("/post/board")
    public String board(Model model, @PageableDefault(page = 0, size = 20) Pageable pageable){

        //모든 게시물 불러오기
        List<PostDto> postList = postService.selectAllPost();

        //페이징
        Page<PostDto> page = postService.paging(postList, pageable);
        model.addAttribute("list", page);

        return "post/board";
    }

    //게시글 상세정보 detail
    @GetMapping("/post/detail/{postId}")
    public String postDetail(@PathVariable Long postId, Model model){
        //Post하나 불러오기
        PostDto postDto = postService.selectOnePost(postId);

        //해당 포스트의 이미지 리스트
        List<PostImgDto> postImgDtoList = postService.selectPostImgs(postId);
        //이미지 아이디만 담은 리스트
        List<Long> postImgIdList = postImgDtoList.stream().map(PostImgDto::getImgId).collect(Collectors.toList());

        //기본이미지 넣을 수 있게 존재하지 않는 id 넣어줌.
        if (postImgIdList.size() == 0) {
           postImgIdList.add(0L);
        }

        //거래희망장소 지도 정보
        MtPlaceDto mtPlaceDto = postService.selectMtPlace(postId);
//        if(mtPlaceDto == null){
//            model.addAttribute("mtPlace", "noMtPlace");
//        } else{
            model.addAttribute("mtPlace", mtPlaceDto);
//        }

        model.addAttribute("post", postDto);
        model.addAttribute("imgIds", postImgIdList);



        return "post/detail";
    }


    @GetMapping("/post/uploadPage")
    public String categoryOption(Model model){

        return "post/upload_page";
    }

    //게시글 업로드
    @PostMapping("/post/upload")
//    @ResponseBody
    public ResponseEntity<String> insPost(PostDto postDto, @RequestParam MultipartFile[] uploadFiles, MtPlaceDto mtPlaceDto, HttpSession session) throws IOException {
//        log.info("컨트롤러단 mtPlaceDto:", postDto);
        System.out.println("컨트롤러단 mtPlaceDto = " + mtPlaceDto);
//        log.info("controller uploadfiles-length {}", uploadFiles.length);

        //제목, 카테고리, 내용 null체크
        if(postDto.getTitle().isEmpty() || postDto.getCategory()==null || postDto.getContent().isEmpty()){
            return new ResponseEntity<>("제목, 카테고리, 제품 설명을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        
        //작성자 정보 세팅
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        postDto.setMember(loginMember);
        postDto.setLoc(loginMember.getLoc());
        //게시글 내용 개행 처리
        postDto.setContent(postDto.getContent().replace("\r\n","<br>")); //줄개행

        //DB에 insert
        String insResult = postService.insertPost(postDto, uploadFiles, mtPlaceDto);
         if(insResult.equals("이미지타입오류")){
             return new ResponseEntity<>("이미지 파일이 아닙니다.",HttpStatus.BAD_REQUEST);
         }



        return new ResponseEntity<>("상품이 게시되었습니다.",HttpStatus.OK);
    }

    //첫번째 이미지 urlresource 반환
    @GetMapping("/post/firstImg/{postId}")
    @ResponseBody
    public UrlResource postFirstImg(@PathVariable Long postId) throws MalformedURLException {

        List<PostImgDto> postImgDtoList = postService.selectPostImgs(postId);
//        log.info("컨트롤러단 postImgDtoList:{}", postImgDtoList);

        String firstImgPath = defaultPostImg;
        if(!postImgDtoList.isEmpty()) {
            firstImgPath = postImgDtoList.get(0).getSavedPath();
        }


        UrlResource urlResource = new UrlResource("file:"+ firstImgPath);
        return urlResource;
    }
    
    //imgId로 urlresource 반환
    @GetMapping("/post/img/{imgId}")
    @ResponseBody
    public UrlResource postImgUrl(@PathVariable Long imgId) throws MalformedURLException {
        String imgPath = defaultPostImg;
        if(imgId != 0) {
            PostImgDto postImgDto = postService.selectOnePostImg(imgId);
            imgPath = postImgDto.getSavedPath();
        }

        UrlResource urlResource = new UrlResource("file:"+ imgPath);
        return urlResource;
    }

    //게시글 삭제
    @PostMapping("/post/remove")
    public ResponseEntity<String> delPost(@RequestParam Long postId){

        postService.deletePost(postId);

        return new ResponseEntity<>("게시글이 삭제되었습니다.",HttpStatus.OK);
    }
    
    //게시글 수정페이지
    @GetMapping("/post/updatePost/{postId}")
    public String udtPostPage(@PathVariable Long postId, Model model){

        //게시글 하나 정보 불러오기
        PostDto postDto = postService.selectOnePost(postId);
        //게시글 내용 개행처리
        postDto.setContent(postDto.getContent().replace("<br>","\r\n"));

        //이미지 리스트 반환
        List<PostImgDto> postImgDtoList = postService.selectPostImgs(postId);


        model.addAttribute("post", postDto);
        model.addAttribute("postImgs", postImgDtoList);



        return "update/update_post";
    }

    //게시글 수정한 것 업로드
    @PostMapping("/post/updatePost/{postId}")
    public ResponseEntity<String> udtPost(PostDto postDto, @RequestParam List<Long> imgIdList, @RequestParam MultipartFile[] uploadFiles, HttpSession session) throws IOException {
        log.info("게시글수정 컨트롤러에 온 postDto:{}", postDto);
        log.info("게시글수정 컨트롤러에 온 imgId:{}", imgIdList);

        //제목, 카테고리, 내용 null체크
        if(postDto.getTitle().isEmpty() || postDto.getCategory()==null || postDto.getContent().isEmpty()){
            return new ResponseEntity<>("제목, 카테고리, 내용을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        //작성자 정보 세팅
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        postDto.setMember(loginMember);
        postDto.setLoc(loginMember.getLoc());
        //게시글 내용 개행 처리
        postDto.setContent(postDto.getContent().replace("\r\n","<br>")); //줄개행


        //기존 이미지 삭제
        for(Long imgId : imgIdList){
            postService.deleteOnePostImg(imgId);
        }

        //DB에 내용 update, 새 이미지 추가
        postService.updatePost(postDto, uploadFiles);

        return new ResponseEntity<>("수정되었습니다.",HttpStatus.OK);

    }


    //hideState 숨김여부 변경
    @PostMapping("/post/hideState")
    public ResponseEntity<String> udtHideState(@RequestParam Long postId, @RequestParam String hideStateName){
//        log.info("컨트롤러 udtHideState()로 넘어온 postId:{}, hideStateName:{}", postId, hideStateName);

        String resultMsg = postService.updateHideState(postId, hideStateName);

        return new ResponseEntity<>(resultMsg, HttpStatus.OK);

    }

    //sellState 판매여부 변경
    @PostMapping("/post/sellState")
    public ResponseEntity<String> udtSellState(@RequestParam Long postId, @RequestParam String sellStateName){
//        log.info("컨트롤러 도착 sellState!!!!!!!postId:{}, sellState:{}", postId, sellStateName);

        String resultMsg = postService.updateSellState(postId, sellStateName);

        if (resultMsg.equals("판매완료")){
//            postService.insertSellList(postId);
        }
        if(resultMsg.equals("판매중")||resultMsg.equals("예약중")){
            //trade에서 delete
            //review 삭제
        }

        return new ResponseEntity<>(resultMsg, HttpStatus.OK);
    }




    //거래후기보내기 클릭시 구매자 선택 페이지
//    @GetMapping("/post/tradeReview/{postId}")
//    public String tradeReviewCheck(@PathVariable Long postId, Model model){
//
//        //trade테이블에 없으면 구매자 선택 페이지
//       Trade selectedBuyer = postService.selectTradeByPost(postId);
////            return "redirect:/post/buyers/"+postId;
//
//        //trade테이블에 있으면 거래후기 적는 페이지로
////        return "redirect:/reviews/buyer?postId="+postId;
//
//        model.addAttribute("selectedBuyer", selectedBuyer);
//
//        return "post/buyerListByPost";
//    }

    //구매자선택 페이지
    @GetMapping("/post/buyers/{postId}")
    public String buyerList(Model model, HttpSession session, @PathVariable Long postId) {
        MemberDto memberDto = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);

        List<ChatRoomDto> chatRoomList = postService.selectBuyersByPost(memberDto, postId);
        model.addAttribute("chatRoomList", chatRoomList);

//        Trade selectedBuyer = postService.selectTradeByPost(postId);
//        if(selectedBuyer == null){
//            model.addAttribute("selectedBuyerId", null);
//        } else {
//            String selectedBuyerId = selectedBuyer.getBuyer().getMemId();
//            model.addAttribute("selectedBuyerId", selectedBuyerId);
//        }

        return "post/buyerListByPost";
    }

    //구매자 선택시
    @GetMapping("/post/buyer/{postId}/{buyerId}")
    public String chooseBuyer(@PathVariable Long postId, @PathVariable String buyerId){

        Trade trade = postService.selectTradeByPost(postId);

        if (trade == null){
            //trade에 없을 경우
            postService.insertTrade(postId, buyerId);

        } else if (trade.getBuyer().getMemId() != buyerId) {
            //trade에 있는 buyer와 다른 buyer를 선택했을 경우
            postService.updateTrade(postId, buyerId);
        } else{
            //trade에 있는 buyer와 같은 buyer를 선택했을 경우
        }

        return "redirect:/reviews/buyer?postId="+postId;
    }

















}
