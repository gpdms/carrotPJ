package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.SellState;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import com.exercise.carrotproject.domain.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        return "board";
    }

    //게시글 상세정보 detail
    @GetMapping("/post/detail/{postId}")
    public String postDetail(@PathVariable Long postId, Model model){
        //Post하나 불러오기
        PostDto postDto = postService.selectOnePost(postId);
//        postDto.setContent(postDto.getContent().replace("<br>","\r\n")); //줄개행
//        log.info("content줄바꿈확인:{}",postDto.getContent());

        //해당 포스트의 이미지 리스트
        List<PostImgDto> postImgDtoList = postService.selectPostImgs(postId);
        //이미지 아이디만 담은 리스트
        List<Long> postImgIdList = postImgDtoList.stream().map(PostImgDto::getImgId).collect(Collectors.toList());
//        log.info("컨트롤러단 이미지아이디 리스트:{}",postImgIdList.size());

        if (postImgIdList.size() == 0) {
           postImgIdList.add(0L);
        }

        model.addAttribute("post", postDto);
        model.addAttribute("imgIds", postImgIdList);


        return "detail";
    }


    @GetMapping("/post/uploadPage")
    public String categoryOption(Model model){

        return "upload_page";
    }

    //게시글 업로드
    @PostMapping("/post/upload")
//    @ResponseBody
    public ResponseEntity<String> insPost(PostDto postDto, @RequestParam MultipartFile[] uploadFiles, HttpSession session) throws IOException {
//        log.info("컨트롤러단 postDto:", postDto);
//        log.info("controller uploadfiles-length {}", uploadFiles.length);

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

        //DB에 insert
        String a = postService.insertPost(postDto, uploadFiles);
         if(a.equals("이미지타입오류")){
             return new ResponseEntity<>("이미지 파일이 아닙니다.",HttpStatus.BAD_REQUEST);
         } else if(a.equals("성공")){
             return new ResponseEntity<>("상품이 게시되었습니다.",HttpStatus.OK);

         }

        return new ResponseEntity<>("상품이 게시되었습니다.",HttpStatus.OK);
    }

    //첫번째 이미지 urlresource 반환
    @GetMapping("/post/firstImg/{postId}")
    @ResponseBody
    public UrlResource postFirstImg(@PathVariable Long postId) throws MalformedURLException {

        List<PostImgDto> postImgDtoList = postService.selectPostImgs(postId);
//        log.info("컨트롤러단 postImgDtoList:{}", postImgDtoList);

        String firstImgPath = "C:/upload/default_image.png";
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
        String imgPath = "C:/upload/default_image.png";

        if(imgId != 0) {
            PostImgDto postImgDto = postService.selectOnePostImg(imgId);
//            log.info("컨트롤러단 postImgDto:{}", postImgDto);
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



        return "update_post";
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

        return new ResponseEntity<>(resultMsg, HttpStatus.OK);
    }



















}
