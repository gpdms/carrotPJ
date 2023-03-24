package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String home(){
        return "redirect:/post/board";
    }

    @GetMapping("/post/board")
    public String board(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable){

        List<PostDto> postList = postService.selectAllPost();
        model.addAttribute("postList", postList);

        //페이징
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), postList.size());
        final Page<PostDto> page = new PageImpl<>(postList.subList(start, end), pageable, postList.size());
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
    public String insPost(PostDto postDto, Model model){
        //사용자 입력 받아오기
//        log.info("postDto: "+postDto);

        //DB에 insert
        postService.insertPost(postDto);

        return "board";
    }




}
