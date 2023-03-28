package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.service.PostService;
import com.exercise.carrotproject.domain.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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
        PostDto postDto = postService.selectOnePost(postId);
        model.addAttribute("post", postDto);
        return "detail";
    }


    @GetMapping("/post/uploadPage")
    public String categoryOption(Model model){

        return "upload_page";
    }


    @PostMapping("/post/upload")
    @ResponseBody
    public String insPost(PostDto postDto, MultipartFile[] uploadFiles, HttpSession session) throws IOException {
//        log.info("postDto: "+postDto);

        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        postDto.setMemberId(loginMember.getMemId());
        postDto.setLoc(loginMember.getLoc());

        postService.insertPost(postDto, uploadFiles);

        return "post/board";
    }

    @GetMapping("/post/imgs/{postId}")
    @ResponseBody
    public UrlResource postImgList(@PathVariable Long postId) throws MalformedURLException {

        List<PostImgDto> postImgDtoList = postService.selectPostImgs(postId);
        log.info("컨트롤러단 postImgDtoList:", postImgDtoList);

        String firstImgPath = postImgDtoList.get(0).getSavedPath();

        UrlResource urlResource = new UrlResource("file:"+ firstImgPath);
        return urlResource;
    }




}
