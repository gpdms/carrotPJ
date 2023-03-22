package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.domain.enumList.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
@Log4j2
public class PostController {

    @PostMapping("/create")
    public String categoryOption(Model model){
        Map<Integer,String> category = Category.codeAndCategory;
        log.info(category);
        model.addAttribute("categoryList", category);


        return "item_create";
    }


}
