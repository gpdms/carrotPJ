package com.exercise.carrotproject.web.img.controller;

import com.exercise.carrotproject.domain.chat.service.ChatService;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import com.exercise.carrotproject.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImgController {
    private final PostService postService;
    private final MemberService memberService;
    private final ChatService chatService;

    @Value("${default.postImg}")
    private String defaultPostImg;

    @Value("${default.pfImg}")
    private String defaultPfImg;

    @Value("${file.postImg}")
    private String rootImgDir;

    //첫번째 이미지 urlresource 반환
    @GetMapping("/post/firstImg/{postId}")
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
    public UrlResource postImgUrl(@PathVariable Long imgId) throws MalformedURLException {
        String imgPath = defaultPostImg;
        if(imgId != 0) {
            PostImgDto postImgDto = postService.selectOnePostImg(imgId);
            imgPath = postImgDto.getSavedPath();
        }
        UrlResource urlResource = new UrlResource("file:"+ imgPath);
        return urlResource;
    }
    //프로필 이미지 출력
    @GetMapping("members/{memId}/profileImg")
    public UrlResource viewProfileImg(@PathVariable("memId") String memId) throws IOException {
        String profPath = memberService.getProfPath(memId);
        if(profPath == null || profPath.isEmpty()) {
//            profPath = rootImgDir+"/pf/profile_img.png";
            profPath = defaultPfImg;
        }
        UrlResource urlResource = new UrlResource("file:" + profPath);
        return urlResource;
    }

    //채팅 이미지 출력
    @ResponseBody
    @GetMapping("chat/chatImg/{chatImgId}")
    public Resource viewProfileImg(@PathVariable("chatImgId") Long chatImgId) throws IOException {
        String imgPath = chatService.getChatImgPath(chatImgId);
        UrlResource urlResource = new UrlResource("file:" + imgPath);
        return urlResource;
    }
}
