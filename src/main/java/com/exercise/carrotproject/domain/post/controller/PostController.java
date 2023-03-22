package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/board")
    public String board(Model model){
        return "board";
    }


    @GetMapping("/uploadPage")
    public String categoryOption(Model model){
        return "item_create";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String insPost(@ModelAttribute PostDto postDto, Model model){
        //사용자 입력 받아오기
//        log.info("postDto: "+postDto);

        //DB에 insert
        postService.insertPost(postDto);

        return "board";
    }




}
