package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
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

    @GetMapping("/post/detail")
    public String detail(){
        return "detail";
    }


    @GetMapping("/post/uploadPage")
    public String categoryOption(Model model){

        return "upload_page";
    }

    @PostMapping("/post/upload")
    @ResponseBody
    public String insPost(PostDto postDto, MultipartFile[] uploadFiles, Model model) throws IOException {
        //사용자 입력 받아오기
//        log.info("postDto: "+postDto);

        //DB에 insert
        postService.insertPost(postDto);

//        for(MultipartFile uploadFile: uploadFiles){
//            String orgName = uploadFile.getOriginalFilename();
//            String savedName = orgName.substring(orgName.lastIndexOf("\\")+1);
//            log.info("fileName:"+savedName);
//            Path savePath = Paths.get("c:\\upload\\"+savedName);  //Path 임포트:java.nio.file.Path
//            uploadFile.transferTo(savePath); //throws 예외처리.
//        }


        return "post/board";
    }




}
