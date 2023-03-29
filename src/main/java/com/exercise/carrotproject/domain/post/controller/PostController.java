package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.SessionConst;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostImg;
import com.exercise.carrotproject.domain.post.service.PostService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
        log.info("컨트롤러단 이미지아이디 리스트:{}",postImgIdList.size());

        model.addAttribute("post", postDto);
        if (postImgIdList.size() == 0) {
           postImgIdList.add(0L);
        }
        model.addAttribute("imgIds", postImgIdList);
        return "detail";
    }


    @GetMapping("/post/uploadPage")
    public String categoryOption(Model model){

        return "upload_page";
    }


    @PostMapping("/post/upload")
//    @ResponseBody
    public ResponseEntity<String> insPost(PostDto postDto, @RequestParam MultipartFile[] uploadFiles, HttpSession session) throws IOException {
//        log.info("postDto: "+postDto);
        log.info("controller uploadfiles-length {}", uploadFiles.length);
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        postDto.setMember(loginMember);
        postDto.setLoc(loginMember.getLoc());
        postDto.setContent(postDto.getContent().replace("\r\n","<br>")); //줄개행

        log.info("컨트롤러단 postDto:", postDto);
        String a = postService.insertPost(postDto, uploadFiles);
         if(a.equals("이미지타입오류")){
             new ResponseEntity<>("이미지 파일이 아닙니다.",HttpStatus.BAD_REQUEST);
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
            log.info("컨트롤러단 postImgDto:{}", postImgDto);
            imgPath = postImgDto.getSavedPath();
        }

        UrlResource urlResource = new UrlResource("file:"+ imgPath);
        return urlResource;
    }

    @GetMapping("/post/remove/{postId}")
    @ResponseBody
    public String delPost(){



        return "post/board";
    }





}
