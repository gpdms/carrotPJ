package com.exercise.carrotproject.web.post.controller;

import com.exercise.carrotproject.domain.chat.dto.ChatRoomDto;
import com.exercise.carrotproject.domain.chat.service.ChatServiceImpl;
import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.member.util.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.BlockService;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.MtPlaceDto;

import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.service.PostService;
import com.exercise.carrotproject.domain.post.service.TradeService;
import com.exercise.carrotproject.web.argumentresolver.Login;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final TradeService tradeService;
    private final ChatServiceImpl chatService;
    private final MemberService memberService;
    private final BlockService blockService;

    @Value("${default.postImg}")
    private String defaultPostImg;

    @GetMapping("/board")
    public String home(){
        return "redirect:/post/board";
    }

    @GetMapping("/post/board")
    public String board(Model model, HttpSession session, @PageableDefault(page = 0, size = 20) Pageable pageable){

        MemberDto member = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //모든 게시물 불러오기
        List<PostDto> postList = postService.selectAllPost(member);

        //페이징
        Page<PostDto> page = postService.paging(postList, pageable);
        
        model.addAttribute("list", page);

        return "post/board";
    }
    @GetMapping("/post/board/{category}")
    public String boardByCategory(@PathVariable String category,
                                     HttpSession session,
                                     @PageableDefault(page = 0, size = 20) Pageable pageable,
                                     Model model) {
        Category selectedCategory = Arrays.stream(Category.values())
                .filter(e -> e.name().equals(category))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not exists Category like categoryName="+category));
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        List<PostDto> postList = postService.selectPostListByCategory(loginMember, selectedCategory);
        model.addAttribute("list", postService.paging(postList, pageable));
        model.addAttribute("category", selectedCategory);
        return "post/categoryList";
    }

    //게시글 상세정보 detail
    @GetMapping("/post/detail/{postId}")
    public String postDetail(@PathVariable Long postId,
                             Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        String memId = ((MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER)).getMemId();
        /* 조회수 로직 */
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        //쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC); // 하루 종료 시간을 시간초로 변환
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);// 현재 시간을 시간초로 변환
        int cookieTime = (int) (todayEndSecond - currentSecond); // 하루 종료까지 남은 시간초

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("["+ memId.toString()+"_"+ postId.toString() +"]")) {
                postService.updateHits(postId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + memId+"_"+ postId + "]");
                oldCookie.setPath("/");  //모든 경로에서 접근 가능
                oldCookie.setMaxAge(cookieTime); // 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            postService.updateHits(postId);
            Cookie newCookie = new Cookie("postView", "[" + memId+"_"+ postId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(cookieTime); 	// 쿠키 시간
            response.addCookie(newCookie);
        }

        //Post하나 불러오기
        PostDto postDto = postService.selectOnePost(postId);
        if(postDto != null && memId != null) {
            boolean hasBlock = blockService.hasBlockByMemIds(postDto.getMember().getMemId(), memId);
            if(hasBlock) {
                return "redirect:/";
            }
        }

        //해당 포스트의 이미지 리스트
        List<PostImgDto> postImgDtoList = postService.selectPostImgs(postId);
        //이미지 아이디만 담은 리스트
        List<Long> postImgIdList = postImgDtoList.stream().map(PostImgDto::getImgId).collect(Collectors.toList());

        //기본이미지 넣을 수 있게 존재하지 않는 id 넣어줌.
        if (postImgIdList.size() == 0) {
           postImgIdList.add(0L);
        }

        //거래희망장소 지도 정보
        MtPlaceDto mtPlaceDto = postService.selectMtPlace(postId);

        //찜 여부, 찜 개수
        String isWishExist = postService.isWishExist(postId, memId);
        Integer countWish = postService.countWish(postId);

        //채팅 개수
        Integer countChatRoom = chatService.countChatRoomByPost(postDto);

        //게시글 작성자가 판매중인 상품 리스트
        String postMemId = postDto.getMember().getMemId();
        Map map = postService.selectPostBySellState(postMemId);
        List<PostDto> onSaleAndRsvList = (List<PostDto>) map.get("onSaleAndRsvList");


        model.addAttribute("post", postDto);
        model.addAttribute("imgIds", postImgIdList);
        model.addAttribute("mtPlace", mtPlaceDto);
        model.addAttribute("isWishExist", isWishExist);
        model.addAttribute("countWish", countWish);
        model.addAttribute("countChatRoom", countChatRoom);
        model.addAttribute("sellerProductList", onSaleAndRsvList);


        return "post/detail";
    }

    @GetMapping("/post/uploadPage")
    public String categoryOption(Model model){
        return "post/upload_page";
    }

    //게시글 업로드
    @PostMapping("/post/upload")
    public ResponseEntity<String> insPost(PostDto postDto, @RequestParam MultipartFile[] uploadFiles, MtPlaceDto mtPlaceDto, HttpSession session) throws IOException {

        //제목, 카테고리, 내용 null체크
        if(postDto.getTitle().isEmpty() || postDto.getCategory()==null || postDto.getContent().isEmpty()){
            return new ResponseEntity<>("제목, 카테고리, 제품 설명을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        //작성자 정보 세팅
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        postDto.setMember(loginMember);
        postDto.setLoc(loginMember.getLoc());
        //게시글 내용 개행 처리
        postDto.setContent(postDto.getContent().replace("\r\n","<br>")); //줄개행

        //거래희망장소 null처리
        if (mtPlaceDto.getLat() == 0 || mtPlaceDto.getLon()==0 || mtPlaceDto.getPlaceInfo()==""){
            mtPlaceDto.setLat(null);
            mtPlaceDto.setLon(null);
            mtPlaceDto.setPlaceInfo(null);
        }

        //DB에 insert
        String insResult = postService.insertPost(postDto, uploadFiles, mtPlaceDto);
         if(insResult.equals("이미지타입오류")){
             return new ResponseEntity<>("이미지 파일이 아닙니다.",HttpStatus.BAD_REQUEST);
         }
        return new ResponseEntity<>("상품이 게시되었습니다.",HttpStatus.OK);
    }

/*    //첫번째 이미지 urlresource 반환
    @GetMapping("/post/firstImg/{postId}")
    @ResponseBody
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
    @ResponseBody
    public UrlResource postImgUrl(@PathVariable Long imgId) throws MalformedURLException {
        String imgPath = defaultPostImg;
        if(imgId != 0) {
            PostImgDto postImgDto = postService.selectOnePostImg(imgId);
            imgPath = postImgDto.getSavedPath();
        }

        UrlResource urlResource = new UrlResource("file:"+ imgPath);
        return urlResource;
    }*/

    //게시글 삭제
    @PostMapping("/post/remove")
    public ResponseEntity<String> delPost(@RequestParam Long postId){

        postService.deletePost(postId);

        return new ResponseEntity<>("게시글이 삭제되었습니다.",HttpStatus.OK);
    }
    
    //게시글 수정페이지
    @GetMapping("/post/updatePost/{postId}")
    public String udtPostPage(@PathVariable Long postId, Model model){

        //게시글 하나 정보 불러오기
        PostDto postDto = postService.selectOnePost(postId);
        //게시글 내용 개행처리
        postDto.setContent(postDto.getContent().replace("<br>","\r\n"));

        //이미지 리스트 반환
        List<PostImgDto> postImgDtoList = postService.selectPostImgs(postId);

        //거래희망장소
        MtPlaceDto mtPlaceDto = postService.selectMtPlace(postId);

        model.addAttribute("post", postDto);
        model.addAttribute("postImgs", postImgDtoList);
        model.addAttribute("mtPlace", mtPlaceDto);



        return "post/update_post";
    }

    //게시글 수정 업로드
    @PostMapping("/post/updatePost/{postId}")
    public ResponseEntity<String> udtPost(PostDto postDto, MtPlaceDto mtPlaceDto, @RequestParam List<Long> imgIdList, @RequestParam MultipartFile[] uploadFiles, HttpSession session) throws IOException {
        log.info("게시글수정 컨트롤러에 온 postDto:{}", postDto);
        log.info("게시글수정 컨트롤러에 온 imgId:{}", imgIdList);
        log.info("게시글수정 컨트롤러에 온 mtPlaceDto:{}", mtPlaceDto);


        //제목, 카테고리, 내용 null체크
        if(postDto.getTitle().isEmpty() || postDto.getCategory()==null || postDto.getContent().isEmpty()){
            return new ResponseEntity<>("제목, 카테고리, 내용을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        //작성자 정보 세팅
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        postDto.setMember(loginMember);
        postDto.setLoc(loginMember.getLoc());
        //게시글 내용 개행 처리
        postDto.setContent(postDto.getContent().replace("\r\n","<br>")); //줄개행


        //기존 이미지 삭제
        for(Long imgId : imgIdList){
            postService.deleteOnePostImg(imgId);
        }

        //DB에 내용 update, 새 이미지 추가
        postService.updatePost(postDto, uploadFiles);

        //거래희망장소 변경
        postService.updateMtPlace(postDto, mtPlaceDto);


        return new ResponseEntity<>("수정되었습니다.",HttpStatus.OK);

    }


    //hideState 숨김여부 변경
    @PostMapping("/post/hideState")
    public ResponseEntity<String> udtHideState(@RequestParam Long postId, @RequestParam String hideStateName){
//        log.info("컨트롤러 udtHideState()로 넘어온 postId:{}, hideStateName:{}", postId, hideStateName);

        String resultMsg = postService.updateHideState(postId, hideStateName);

        return new ResponseEntity<>(resultMsg, HttpStatus.OK);

    }

    //sellState 판매여부 변경
    @PostMapping("/post/sellState")
    public ResponseEntity<String> udtSellState(@RequestParam Long postId, @RequestParam String sellStateName){
//        log.info("컨트롤러 도착 sellState!!!!!!!postId:{}, sellState:{}", postId, sellStateName);

        String resultMsg = postService.updateSellState(postId, sellStateName);

        if (resultMsg.equals("판매완료")){
        }
        if(resultMsg.equals("판매중")||resultMsg.equals("예약중")){
            //trade에서 delete
            //review 삭제
            tradeService.deleteTradeAndReview(postId);
        }

        return new ResponseEntity<>(resultMsg, HttpStatus.OK);
    }




    //구매자선택 페이지
    @GetMapping("/post/buyers/{postId}")
    public String buyerList(Model model, HttpSession session, @PathVariable Long postId) {
        MemberDto memberDto = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);

        List<ChatRoomDto> chatRoomList = postService.selectBuyersByPost(memberDto, postId);
        model.addAttribute("chatRoomList", chatRoomList);

        return "post/buyerListByPost";
    }
    //구매자 선택시
    @GetMapping("/post/buyer/{postId}/{buyerId}")
    public String chooseBuyer(@PathVariable Long postId, @PathVariable String buyerId){

        Trade trade = tradeService.selectTradeByPost(postId);

        if (trade == null){
            //trade에 없을 경우
            tradeService.insertTrade(postId, buyerId);

        } else if (trade.getBuyer().getMemId() != buyerId) {
            //trade에 있는 buyer와 다른 buyer를 선택했을 경우
            tradeService.updateTrade(postId, buyerId);
        }

        return "redirect:/reviews/buyer?postId="+postId;
    }


    //찜하기
    @PostMapping("/post/addWish")
    @ResponseBody
    public String insWish(@RequestParam Long postId, @RequestParam String memId){
//        log.info("컨트롤러에 온 postId:{}, memId:{}",postId, memId);
        postService.insertWish(postId, memId);

        return "관심목록에 추가되었습니다.";
    }

    //찜 해제하기
    @PostMapping("/post/rmvWish")
    @ResponseBody
    public String dltWish(@RequestParam Long postId, @RequestParam String memId){
//        log.info("컨트롤러에 온 postId:{}, memId:{}",postId, memId);
        postService.deleteWish(postId, memId);

        return "관심목록에서 제거되었습니다.";
    }

    //한 판매자가 판매중인 상품
    @GetMapping("/post/onSale/{memId}")
    public String onSalePost(@PathVariable String memId, Model model, @PageableDefault(page = 0, size = 20)Pageable pageable){
        //판매자 정보
        Member member = memberService.findMemberByMemId(memId);
        MemberDto seller = MemberEntityDtoMapper.toDto(member);
        //판매중,예약중 게시글
        Map map = postService.selectPostBySellState(memId);
        List<PostDto> onSaleAndRsvList = (List) map.get("onSaleAndRsvList");
        //페이징
        Page<PostDto> page = postService.paging(onSaleAndRsvList, pageable);

        model.addAttribute("seller", seller);
        model.addAttribute("list", page);
        return "post/sellerSellList";
    }

    @GetMapping("/post/search")
    public String searchPost(@RequestParam String word,
                             @PageableDefault(page = 0, size = 12) Pageable pageable,
                             @Login MemberDto loginMember, Model model) {
        String loginMemId = "";
        if (loginMember != null) {
            loginMemId = loginMember.getMemId();
        }
        List<PostDto> postList = postService.searchPostList(loginMemId, word);
        Page<PostDto> paginatedPostList = postService.paging(postList, pageable);
        model.addAttribute("postList", paginatedPostList);
        model.addAttribute("searchedWord", word);
        return "post/searchList";
    }


}
