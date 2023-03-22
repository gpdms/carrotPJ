package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Controller
@Log4j2
public class PostController {

    @PersistenceContext
    EntityManager em;

    @GetMapping("/uploadPage")
    public String categoryOption(Model model){
        //카테고리
        Map<String,String> category = Category.codeAndCategory;
        log.info(category);
        model.addAttribute("categoryList", category);

        return "item_create";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String insPost(@RequestBody Post post, Model model){
        //사용자 입력 받아오기
        log.info(post);

        return "item_create";
    }


}
