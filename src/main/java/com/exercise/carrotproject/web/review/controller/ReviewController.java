package com.exercise.carrotproject.web.review.controller;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.service.TradeService;
import com.exercise.carrotproject.domain.review.dto.AddReviewRequest;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.dto.ReviewResponse;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.service.*;
import com.exercise.carrotproject.web.argumentresolver.Login;
import com.exercise.carrotproject.web.member.error.ErrorCode;
import com.exercise.carrotproject.web.member.error.ErrorResponse;
import com.exercise.carrotproject.web.review.form.ReviewForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Slf4j
@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final MemberService memberService;
    private final TradeService tradeService;
    private final ReviewSellerService reviewSellerService;
    private final ReviewBuyerService reviewBuyerService;
    private final ReviewService reviewService;
    private final ReviewBuyerRepository reviewBuyerRepository;

    @GetMapping("/{memId}")
    public String toPublicReviewMessagesDetail(@PathVariable final String memId, Model model) {
        Map<String, List<ReviewMessageDto>> messageMap = reviewService.collectGoodReviewMessages(memId);
        model.addAttribute("messageMap", messageMap);
        model.addAttribute("nickname",  memberService.findMemberByMemId(memId).getNickname());
        return "/review/publicReviews";
    }

    @GetMapping("/buyer")
    public String toReviewBuyerForm(@RequestParam final Long postId,
                                    @Login final MemberDto loginMember,
                                    RedirectAttributes redirectAttributes, Model model){
        if(reviewBuyerService.existsReviewBuyerByPostId(postId)) { //이미 등록된 판매자 리뷰가 있으면 판매완료 페이지로
            redirectAttributes.addAttribute("me", loginMember.getMemId());
            return "redirect:/members/{me}/trade/sell-list";
        }
        Trade sellOne = tradeService.findTradeByPostId(postId);
        Member seller = sellOne.getSeller();
        Member buyer = sellOne.getBuyer();
        ReviewForm reviewForm = ReviewForm.builder()
                .sellerId(seller.getMemId())
                .sellerNick(seller.getNickname())
                .buyerId(buyer.getMemId())
                .buyerNick(buyer.getNickname())
                .postId(postId)
                .build();
        model.addAttribute("reviewForm", reviewForm);
        return "review/reviewForm";
    }

    @PostMapping("/buyer")
    public ResponseEntity addBuyerReview(@RequestBody final ReviewForm form) {
        boolean hasReview = reviewBuyerService.existsReviewBuyerByPostId(form.getPostId());
        if(hasReview) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.EXISTS_REVIEW));
        }
        AddReviewRequest addReviewRequest = AddReviewRequest.builder()
                .sellerId(form.getSellerId())
                .buyerId(form.getBuyerId())
                .postId(form.getPostId())
                .reviewStateCode(form.getReviewStateCode())
                .indicatorNames(form.getIndicators())
                .message(form.getMessage())
                .build();
        Long reviewBuyerId = reviewBuyerService.insertReviewBuyer(addReviewRequest);
        return ResponseEntity.ok(reviewBuyerId);
    }

    @GetMapping("/buyer/{reviewBuyerId}")
    public String viewOneReviewBuyer (@PathVariable final Long reviewBuyerId,
                                     @Login final MemberDto loginMember,
                                     Model model) {
//        ReviewResponse reviewResponse = reviewBuyerService.getReviewResponseById(reviewBuyerId);
        ReviewBuyer reviewBuyer = reviewBuyerRepository.findById(reviewBuyerId)
                .orElseThrow(() -> new NoSuchElementException("ReviewBuyer Not Found"));
        ReviewResponse reviewResponse = ReviewResponse.of(reviewBuyer);

        boolean isReviewer = loginMember.getMemId().equals(reviewResponse.getBuyerId()) ? false : true;
        model.addAttribute("isReviewer", isReviewer);
        model.addAttribute("review", reviewResponse);
       return "review/oneReview";
    }

    @DeleteMapping("/buyer/{reviewBuyerId}")
    public ResponseEntity deleteReviewBuyer(@PathVariable final Long reviewBuyerId) {
        reviewBuyerService.deleteReviewBuyerById(reviewBuyerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/seller")
    public String toReviewSellerForm(@RequestParam final Long postId,
                                     @Login final MemberDto loginMember,
                                     RedirectAttributes redirectAttributes, Model model){
        if(reviewSellerService.existsReviewSellerByPostId(postId)) { //이미 등록한 구매자 리뷰가 있으면 나의 구매 목록으로
            redirectAttributes.addAttribute("me", loginMember.getMemId());
            return "redirect:/members/{me}/trade/buy-list";
        }
        Trade trade = tradeService.findTradeByPostId(postId);
        Member seller = trade.getSeller();
        Member buyer = trade.getBuyer();
        ReviewForm reviewForm = ReviewForm.builder()
                .sellerId(seller.getMemId())
                .sellerNick(seller.getNickname())
                .buyerId(buyer.getMemId())
                .buyerNick(buyer.getNickname())
                .postId(postId)
                .build();
       model.addAttribute("reviewForm", reviewForm);
        return "review/reviewForm";
    }

    @PostMapping("/seller")
    public ResponseEntity addReviewSeller(@RequestBody final ReviewForm form) {
        boolean hasReview = reviewSellerService.existsReviewSellerByPostId(form.getPostId());
        if(hasReview) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.EXISTS_REVIEW));
        }
        AddReviewRequest reviewSellerRequest = AddReviewRequest.builder()
                .sellerId(form.getSellerId())
                .buyerId(form.getBuyerId())
                .postId(form.getPostId())
                .reviewStateCode(form.getReviewStateCode())
                .indicatorNames(form.getIndicators())
                .message(form.getMessage())
                .build();
        Long reviewSellerId = reviewSellerService.insertReviewSeller(reviewSellerRequest);
        return ResponseEntity.ok(reviewSellerId);
    }

    @GetMapping("/seller/{reviewSellerId}")
    public String viewOneReviewSeller (@PathVariable final Long reviewSellerId,
                                      @Login final MemberDto loginMember,
                                      Model model) {
        ReviewResponse reviewResponse = reviewSellerService.getReviewResponseById(reviewSellerId);
        boolean isReviewer = loginMember.getMemId().equals(reviewResponse.getSellerId()) ? false : true;
        model.addAttribute("isReviewer", isReviewer);
        model.addAttribute("review", reviewResponse);
        return "review/oneReview";
    }

    @DeleteMapping("/seller/{reviewSellerId}")
    public ResponseEntity deleteSellerReview(@PathVariable final Long reviewSellerId) {
       reviewSellerService.deleteReviewSellerById(reviewSellerId);
       return ResponseEntity.ok().build();
    }

    @GetMapping("/manner/{memId}")
    public String toMannerDetail(@PathVariable final String memId, Model model) {
        model.addAttribute("positiveMannerMap", reviewService.getPositiveReviewIndicators(memId));
        model.addAttribute("negativeMannerMap", reviewService.getNegativeReviewIndicators(memId));
        model.addAttribute("nickname", memberService.findMemberByMemId(memId).getNickname());
        return "review/mannerDetail";
    }

    @PostMapping("/seller/hide")
    public ResponseEntity hideSellerReviewMessage(@RequestParam Long reviewSellerId) {
        reviewSellerService.hideReviewSeller(reviewSellerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/buyer/hide")
    public ResponseEntity hideBuyerReviewMessage(@RequestParam Long reviewBuyerId) {
        reviewBuyerService.hideReviewBuyer(reviewBuyerId);
        return ResponseEntity.ok().build();
    }
}
