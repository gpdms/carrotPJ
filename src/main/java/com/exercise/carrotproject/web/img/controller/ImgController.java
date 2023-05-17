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
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
            profPath = defaultPfImg;
        }
        UrlResource urlResource = new UrlResource("file:" + profPath);
        return urlResource;
    }
  /*  @GetMapping("members/{memId}/profileImg")
    public ResponseEntity<UrlResource> viewProfileImg(@PathVariable("memId") String memId)
            throws IOException {
        String profPath = memberService.getProfPath(memId);
        if (profPath == null || profPath.isEmpty()) {
            profPath = defaultPfImg;
        }
        UrlResource urlResource = new UrlResource("file:" + profPath);
        // 이미지 캐싱 설정
        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS); // 1일 동안 캐시 유지
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(urlResource);
    }*/

    //채팅 이미지 출력
    @ResponseBody
    @GetMapping("chat/chatImg/{chatImgId}")
    public Resource viewChatImg(@PathVariable("chatImgId") Long chatImgId) throws IOException {
        String imgPath = chatService.getChatImgPath(chatImgId);
        UrlResource urlResource = new UrlResource("file:" + imgPath);
        return urlResource;
    }
}
