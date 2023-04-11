package com.exercise.carrotproject.web.review.controller;

import com.exercise.carrotproject.domain.enumList.*;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.domain.post.entity.BuyList;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.SellList;
import com.exercise.carrotproject.domain.post.repository.BuyListRepository;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.repository.SellListRepository;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerCustomRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerCustomRepository;
import com.exercise.carrotproject.domain.review.service.ReviewBuyerServiceImpl;
import com.exercise.carrotproject.domain.review.service.ReviewSellerServiceImpl;
import com.exercise.carrotproject.domain.review.service.ReviewServiceImpl;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.review.form.ReviewDetailForm;
import com.exercise.carrotproject.web.review.form.ReviewForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


@Slf4j
@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final MemberServiceImpl memberService;
    private final ReviewSellerServiceImpl reviewSellerService;
    private final ReviewBuyerServiceImpl reviewBuyerService;
    private final ReviewServiceImpl reviewService;

    private final PostRepository postRepository;
    private final BuyListRepository buyListRepository;
    private final SellListRepository sellListRepository;

    private final ReviewBuyerCustomRepository reviewBuyerCustomRepository;
    private final ReviewSellerCustomRepository reviewSellerCustomRepository;

    //테스트 db
   @GetMapping
   @ResponseBody
   public void toReviewBuyerForm(HttpServletRequest request) {
       Member mem1 = memberService.findOneMember("tester1");
       Member mem2 = memberService.findOneMember("tester2");
       Member mem3 = memberService.findOneMember("tester3");
       for(int i = 1 ; i <= 20; i++ ){
           if(i% 4  == 0 ){
               Post postBuild1 = Post.builder().title("글" + i).member(mem1).price(i * 1000).category(Category.ETC).loc(mem1.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
               Post post1 = postRepository.save(postBuild1);
               SellList sellList = SellList.builder().post(post1).buyer(mem2).seller(mem1).build();
               sellListRepository.save(sellList);
           } else if(i% 4  == 1 ) {
               Post postBuild2 = Post.builder().title("글" + i).member(mem2).price(i * 1000).category(Category.DIGITAL_DEVICE).loc(mem2.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
               Post post2 = postRepository.save(postBuild2);
               SellList sellList = SellList.builder().post(post2).buyer(mem3).seller(mem2).build();
               sellListRepository.save(sellList);
           } else if(i% 4  == 2 ) {
               Post postBuild3 = Post.builder().title("글" + i).member(mem3).price(i * 1000).category(Category.BOOK).loc(mem3.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
               Post post2 = postRepository.save(postBuild3);
               SellList sellList = SellList.builder().post(post2).buyer(mem1).seller(mem3).build();
               sellListRepository.save(sellList);
           } else {
               Post postBuild2 = Post.builder().title("글" + i).member(mem2).price(i * 1000).category(Category.FOOD).loc(mem2.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
               Post post2 = postRepository.save(postBuild2);
               SellList sellList = SellList.builder().post(post2).buyer(mem1).seller(mem2).build();
               sellListRepository.save(sellList);
           }
       }
   }

    @GetMapping("/{memId}")
    public String toPublicReviewDetail(@PathVariable String memId, Model model) {
        model.addAttribute("messageMap",reviewService.goodReviewMessagesDetail(memId));
        return "review/publicReviewDetail";
    }

    @GetMapping("/buyer")
    public String toBuyerReviewForm(@RequestParam String postId,  HttpSession session, RedirectAttributes redirectAttributes, Model model){
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Post post = postRepository.findById(Long.valueOf(postId)).orElseThrow(() -> new NoSuchElementException("Post Not Found"));
        if(reviewBuyerService.findReviewBuyerIdByPost(post) != 0L) { //이미 등록된 판매자 리뷰가 있으면 구매목록 페이지로
            redirectAttributes.addAttribute("me", loginMember.getMemId());
            return "redirect:/members/{me}/transaction/sellList";
        }
        SellList sellOne = sellListRepository.findByPost(post);
        ReviewForm reviewForm = ReviewForm.builder().sellerId(loginMember.getMemId())
                .buyerId(sellOne.getBuyer().getMemId())
                .postId(Long.valueOf(postId))
                .build();
        model.addAttribute("reviewForm", reviewForm);
        return "review/reviewForm";
    }
    @PostMapping("/buyer")
    @ResponseBody
    public String addBuyerReview(@RequestBody ReviewForm reviewForm, RedirectAttributes redirectAttributes) {
        Post post = postRepository.findById(reviewForm.getPostId()).orElseThrow(() -> new NoSuchElementException("Post Not Found"));
        if(reviewBuyerService.findReviewBuyerIdByPost(post) != 0L) { //이미 등록된 판매자 리뷰가 있으면 구매목록 페이지로
            redirectAttributes.addAttribute("me", reviewForm.getSellerId());
            return "redirect:/members/{me}/transaction/sellList";
        }
        List<ReviewBuyerIndicator> indicatorList = ReviewBuyerIndicator.findAllByEnumName(reviewForm.getIndicators());
        reviewForm.setMessage(reviewForm.getMessage().replace("\r\n","<br>"));
        ReviewBuyer reviewBuyer = ReviewBuyer.builder()
                .seller(memberService.findOneMember(reviewForm.getSellerId()))
                .buyer(memberService.findOneMember(reviewForm.getBuyerId()))
                .post(postRepository.findById(reviewForm.getPostId()).orElse(null))
                .reviewState(ReviewState.findByStateCode(reviewForm.getReviewStateCode()))
                .totalScore(ReviewBuyerIndicator.sumScore(indicatorList))
                .message(reviewForm.getMessage())
                .build();
        reviewBuyerService.insertReviewBuyer(reviewBuyer, indicatorList);
        return "review/reviewForm";
    }
    @GetMapping("/buyer/{reviewBuyerId}")
    public String reviewBuyerDetail (@PathVariable String reviewBuyerId,
                                     HttpSession session,
                                     Model model) {
       ReviewBuyer reviewBuyer = reviewBuyerService.findOneReviewBuyer(Long.valueOf(reviewBuyerId));
        ReviewDetailForm detailForm = ReviewDetailForm.builder()
                .postTitle(reviewBuyer.getPost().getTitle())
                .reviewState(reviewBuyer.getReviewState())
                .buyerId(reviewBuyer.getBuyer().getMemId())
                .sellerId(reviewBuyer.getSeller().getMemId())
                .reviewBuyerId(Long.valueOf(reviewBuyerId))
                .buyerIndicatorList(reviewBuyerService.getReviewBuyerIndicatorsByReview(reviewBuyer))
                .message(reviewBuyer.getMessage())
                .build();
        MemberDto loginMember = (MemberDto)session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("isReviewer", loginMember.getMemId().equals(detailForm.getBuyerId())? false : true);
        model.addAttribute("reviewDetailForm", detailForm);
        return "review/reviewDetail";
    }
    @DeleteMapping("/buyer/{reviewBuyerId}")
    public ResponseEntity<Map<String, Object>> deleteBuyerReview(@PathVariable String reviewBuyerId) {
        reviewBuyerService.deleteReviewBuyer(Long.valueOf(reviewBuyerId));
        return new ResponseEntity<>(Collections.singletonMap("message", "삭제에 성공했습니다."), HttpStatus.OK);
    }

    @GetMapping("/seller")
    public String toSellerReviewForm(@RequestParam String postId,  HttpSession session, RedirectAttributes redirectAttributes, Model model){
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Post post = postRepository.findById(Long.valueOf(postId)).orElseThrow(() -> new NoSuchElementException("Post Not Found"));
        if(reviewSellerService.findReviewSellerIdByPost(post) != 0L) { //이미 등록한 구매자 리뷰가 있으면 나의 구매 목록으로
            redirectAttributes.addAttribute("me", loginMember.getMemId());
            return "redirect:/members/{me}/transaction/buyList";
        }
        BuyList buyOne = buyListRepository.findByPost(post);
       ReviewForm reviewForm= ReviewForm.builder().sellerId(buyOne.getSeller().getMemId())
                .buyerId(loginMember.getMemId())
                .postId(Long.valueOf(postId))
                .build();
       model.addAttribute("reviewForm", reviewForm);
        return "review/reviewForm";
    }
    @PostMapping("/seller")
    @ResponseBody
    public String addSellerReview(@RequestBody ReviewForm reviewForm, RedirectAttributes redirectAttributes) {
        Post post = postRepository.findById(reviewForm.getPostId()).orElseThrow(() -> new NoSuchElementException("Post Not Found"));
        if(reviewSellerService.findReviewSellerIdByPost(post) != 0L) { //이미 등록한 구매자 리뷰가 있으면 나의 구매 목록으로
            redirectAttributes.addAttribute("me", reviewForm.getBuyerId());
            return "redirect:/members/{me}/transaction/buyList";
        }
        List<ReviewSellerIndicator> indicatorList = ReviewSellerIndicator.findAllByEnumName(reviewForm.getIndicators());
        reviewForm.setMessage(reviewForm.getMessage().replace("\r\n","<br>")); //줄개행);
        ReviewSeller reviewSeller = ReviewSeller.builder()
                .seller(memberService.findOneMember(reviewForm.getSellerId()))
                .buyer(memberService.findOneMember(reviewForm.getBuyerId()))
                .post(postRepository.findById(reviewForm.getPostId()).orElse(null))
                .reviewState(ReviewState.findByStateCode(reviewForm.getReviewStateCode()))
                .totalScore(ReviewSellerIndicator.sumScore(indicatorList))
                .message(reviewForm.getMessage())
                .build();
        reviewSellerService.insertReviewSeller(reviewSeller, indicatorList);
        return "성공";
    }
    @GetMapping("/seller/{reviewSellerId}")
    public String reviewSellerDetail (@PathVariable String reviewSellerId,
                                      HttpSession session,
                                      Model model) {
        ReviewSeller reviewSeller = reviewSellerService.findOneReviewSeller(Long.valueOf(reviewSellerId));
        ReviewDetailForm detailForm = ReviewDetailForm.builder()
                .postTitle(reviewSeller.getPost().getTitle())
                .reviewState(reviewSeller.getReviewState())
                .buyerId(reviewSeller.getBuyer().getMemId())
                .sellerId(reviewSeller.getSeller().getMemId())
                .reviewSellerId(Long.valueOf(reviewSellerId))
                .sellerIndicatorList(reviewSellerService.getReviewSellerIndicatorsByReview(reviewSeller))
                .message(reviewSeller.getMessage())
                .build();
        MemberDto loginMember = (MemberDto)session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("isReviewer", loginMember.getMemId().equals(detailForm.getSellerId())? false : true);
        model.addAttribute("reviewDetailForm", detailForm);
        return "review/reviewDetail";
    }
    @DeleteMapping("/seller/{reviewSellerId}")
    public ResponseEntity<Map<String, Object>> deleteSellerReview(@PathVariable String reviewSellerId) {
       reviewSellerService.deleteReviewSeller(Long.valueOf(reviewSellerId));
       return new ResponseEntity<>(Collections.singletonMap("message", "삭제에 성공했습니다."), HttpStatus.OK);
    }

    //매너 지표 디테일
    @GetMapping("/manner/{memId}")
    public String toMannerDetail(@PathVariable String memId, Model model) {
        Map<String, Map<Object, Long>> mannerDetailMap = reviewService.getMannerDetailMap(memId);
        model.addAttribute("positiveMannerMap", mannerDetailMap.get("positiveMannerMap"));
        model.addAttribute("negativeMannerMap", mannerDetailMap.get("negativeMannerMap"));
        return  "review/mannerDetail";
    }

    //숨김기능
    @PostMapping("/seller/hide")
    @ResponseBody
    public ResponseEntity<Map<String, String>> hideSellerReviewMessage(@RequestParam String reviewSellerId) {
        System.out.println("숨김 reviewSellerId = " + reviewSellerId);
        Map<String, String> resultMap = reviewSellerService.hideReviewSeller(Long.valueOf(reviewSellerId));
        if(resultMap.containsKey("fail")) {
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
    @PostMapping("/buyer/hide")
    @ResponseBody
    public ResponseEntity<Map<String, String>> hideBuyerReviewMessage(@RequestParam String reviewBuyerId) {
        System.out.println("숨김 reviewBuyerId = " + reviewBuyerId);
        Map<String, String> resultMap = reviewBuyerService.hideReviewBuyer(Long.valueOf(reviewBuyerId));
        if(resultMap.containsKey("fail")) {
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
