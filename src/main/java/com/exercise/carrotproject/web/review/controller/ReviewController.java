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
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.service.ReviewBuyerServiceImpl;
import com.exercise.carrotproject.domain.review.service.ReviewSellerServiceImpl;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.review.form.ReviewDetailForm;
import com.exercise.carrotproject.web.review.form.ReviewForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    private final PostRepository postRepository;
    private final BuyListRepository buyListRepository;
    private final SellListRepository sellListRepository;

    //테스트 db
   //@GetMapping
   @ResponseBody
   public void toReviewBuyerForm(HttpServletRequest request) {
       Member seller1 = memberService.findOneMember("tester2");
       Member seller2 = memberService.findOneMember("tester3");
       Member buyer = memberService.findOneMember("tester1");

       for(int i = 1 ; i <= 10; i++ ){
           if(i% 2  == 0 ){
               Post postBuild1 = Post.builder().title("글" + i).member(seller1).price(i * 1000).category(Category.ETC).loc(seller1.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
               Post post1 = postRepository.save(postBuild1);
               SellList sellList = SellList.builder().post(post1).buyer(buyer).seller(seller1).build();
               sellListRepository.save(sellList);
           } else {
               Post postBuild2 = Post.builder().title("글" + i).member(seller2).price(i * 1000).category(Category.DIGITAL_DEVICE).loc(seller2.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
               Post post2 = postRepository.save(postBuild2);
               SellList sellList = SellList.builder().post(post2).buyer(seller1).seller(seller2).build();
               sellListRepository.save(sellList);
           }
       }

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
    public String reviewSellerDetail (@PathVariable String reviewSellerId, Model model) {
        ReviewSeller reviewSeller = reviewSellerService.findOneReviewSeller(Long.valueOf(reviewSellerId));
        ReviewDetailForm detailForm = ReviewDetailForm.builder()
                .postTitle(reviewSeller.getPost().getTitle())
                .reviewState(reviewSeller.getReviewState())
                .buyerId(reviewSeller.getBuyer().getMemId())
                .sellerId(reviewSeller.getSeller().getMemId())
                .reviewSellerId(Long.valueOf(reviewSellerId))
                .sellerIndicatorList(reviewSellerService.getReviewSellerIndicatorsByReview(reviewSeller))
                .build();
        model.addAttribute("reviewDetailForm", detailForm);
        return "review/reviewDetail";
    }
    @DeleteMapping("/seller/{reviewSellerId}")
    public ResponseEntity<Map<String, Object>> deleteSellerReview(@PathVariable String reviewSellerId) {
       reviewSellerService.deleteReviewSeller(Long.valueOf(reviewSellerId));
       return new ResponseEntity<>(Collections.singletonMap("message", "삭제에 성공했습니다."), HttpStatus.OK);
    }


   @GetMapping("/buyer")
   public String toBuyerReviewForm(@RequestParam String postId,  HttpSession session, RedirectAttributes redirectAttributes, Model model){
       MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
       Post post = postRepository.findById(Long.valueOf(postId)).orElseThrow(() -> new NoSuchElementException("Post Not Found"));
       if(reviewBuyerService.findReviewBuyerIdByPost(post) != 0L) { //이미 등록된 등록된 판매자 리뷰가 있으면 구매목록 페이지로
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
        if(reviewBuyerService.findReviewBuyerIdByPost(post) != 0L) {
            redirectAttributes.addAttribute("me", reviewForm.getSellerId());
            return "redirect:/members/{me}/transaction/sellList";
        }
        List<ReviewBuyerIndicator> indicatorList = ReviewBuyerIndicator.findAllByEnumName(reviewForm.getIndicators());
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
    public String reviewBuyerDetail (@PathVariable String reviewBuyerId, Model model) {
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
        model.addAttribute("reviewDetailForm", detailForm);
        return "review/reviewDetail";
    }
    @DeleteMapping("/buyer/{reviewBuyerId}")
    public ResponseEntity<Map<String, Object>> deleteBuyerReview(@PathVariable String reviewBuyerId) {
        reviewBuyerService.deleteReviewBuyer(Long.valueOf(reviewBuyerId));
        return new ResponseEntity<>(Collections.singletonMap("message", "삭제에 성공했습니다."), HttpStatus.OK);
    }



}
