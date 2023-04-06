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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.NoSuchElementException;


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
   @GetMapping
   @ResponseBody
   public void toReviewBuyerForm(HttpServletRequest request) {
       Member seller1 = memberService.findOneMember("tester2");
       Member seller2 = memberService.findOneMember("tester3");
       Member buyer = memberService.findOneMember("tester1");

       for(int i = 1 ; i <= 10; i++ ){
           if(i% 2  == 0 ){
               Post postBuild1 = Post.builder().title("글" + i).member(seller1).price(i * 1000).category(Category.ETC).loc(seller1.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
               Post post1 = postRepository.save(postBuild1);
               BuyList buyList= BuyList.builder().post(post1).buyer(buyer).seller(seller1).build();
               buyListRepository.save(buyList);
               SellList sellList = SellList.builder().post(post1).buyer(buyer).seller(seller1).build();
               sellListRepository.save(sellList);
           } else {
               Post postBuild2 = Post.builder().title("글" + i).member(seller2).price(i * 1000).category(Category.DIGITAL_DEVICE).loc(seller2.getLoc()).hideState(HideState.SHOW).sellState(SellState.ON_SALE).content("내용" + i).build();
               Post post2 = postRepository.save(postBuild2);
               BuyList buyList= BuyList.builder().post(post2).buyer(buyer).seller(seller2).build();
               buyListRepository.save(buyList);
               SellList sellList = SellList.builder().post(post2).buyer(buyer).seller(seller2).build();
               sellListRepository.save(sellList);
           }
       }

   }
    @GetMapping("/seller")
    public String toSellerReviewForm(
            HttpSession session,
            @RequestParam String postId,
            Model model){
       MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
       BuyList buyOne = buyListRepository.findByPost(postRepository.findById(Long.valueOf(postId)).orElseThrow());
       ReviewForm reviewForm= ReviewForm.builder().sellerId(buyOne.getSeller().getMemId())
                .buyerId(loginMember.getMemId())
                .postId(Long.valueOf(postId))
                .build();
       model.addAttribute("reviewForm", reviewForm);
        return "review/reviewForm";
    }

    @PostMapping("/seller")
    @ResponseBody
    public String addSellerReview(@RequestBody ReviewForm reviewForm) {
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


   @GetMapping("/buyer")
   public String toBuyerReviewForm(
           HttpSession session,
           @RequestParam String postId,
           Model model){
       MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
       SellList sellOne = sellListRepository.findByPost(postRepository.findById(Long.valueOf(postId)).orElseThrow());
       ReviewForm reviewForm = ReviewForm.builder().sellerId(loginMember.getMemId())
               .buyerId(sellOne.getBuyer().getMemId())
               .postId(Long.valueOf(postId))
               .build();
       model.addAttribute("reviewForm", reviewForm);
       return "review/reviewForm";
   }
    @PostMapping("/buyer")
    @ResponseBody
    public String addBuyerReview(@RequestBody ReviewForm reviewForm) {
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


}
