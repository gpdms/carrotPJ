package com.exercise.carrotproject.domain.post.controller;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.service.MyPageServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageServiceImpl myPageService;

    @GetMapping("/mySellList/{memId}")
    public String mySellList(@PathVariable String memId, Model model){

        //판매중,예약중 게시글
        Map map = myPageService.selectPostBySellState(memId);
        List<PostDto> onSaleAndRsvList = (List) map.get("onSaleAndRsvList");
        List<PostDto> soldList = (List) map.get("soldList");

        model.addAttribute("onSaleAndRsv", onSaleAndRsvList);
        model.addAttribute("soldList", soldList);
        
        //숨김 게시글
        List<PostDto> hidePostList = myPageService.selectHidePost(memId);
        model.addAttribute("hidePostList", hidePostList);

        log.info("숨김 게시글들>>>>>>>>>{}",hidePostList);
        return "myPage/sellList";
    }


}
