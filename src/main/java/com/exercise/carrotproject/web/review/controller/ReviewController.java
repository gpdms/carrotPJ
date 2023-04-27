package com.exercise.carrotproject.web.review.controller;

import com.exercise.carrotproject.domain.enumList.*;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.repository.TradeRepository;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyerDetail;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerCustomRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerCustomRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewBuyerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerRepository;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static java.util.Arrays.asList;


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
    private final TradeRepository tradeRepository;

    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewBuyerCustomRepository reviewBuyerCustomRepository;
    private final ReviewSellerCustomRepository reviewSellerCustomRepository;
    private final ReviewSellerDetailRepository reviewSellerDetailRepository;
    private final ReviewBuyerDetailRepository reviewBuyerDetailRepository;

    @GetMapping("/{memId}")
    public String toPublicReviewMessagesDetail(@PathVariable String memId, Model model) {
        model.addAttribute("messageMap",reviewService.goodReviewMessagesDetail(memId));
        return "review/publicReviewDetail";
    }

    @GetMapping("/buyer")
    public String toBuyerReviewForm(@RequestParam String postId,  HttpSession session, RedirectAttributes redirectAttributes, Model model){
       MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Post post = postRepository.findById(Long.valueOf(postId)).orElseThrow(() -> new NoSuchElementException("Post Not Found"));
/*        if(reviewBuyerService.findReviewBuyerIdByPost(post) != 0L) { //이미 등록된 판매자 리뷰가 있으면 판매완료 페이지로
            redirectAttributes.addAttribute("me", loginMember.getMemId());
            return "redirect:/members/{me}/trade/sellList";
        }*/
        Trade sellOne = tradeRepository.findByPost(post);
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
       /*if(reviewBuyerService.findReviewBuyerIdByPost(post) != 0L) { //이미 등록된 판매자 리뷰가 있으면 판매완료 페이지로
            redirectAttributes.addAttribute("me", reviewForm.getSellerId());
            return "redirect:/members/{me}/trade/sellList";
        }*/
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
        return "성공";
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
            return "redirect:/members/{me}/trade/buyList";
        }
        Trade buyOne = tradeRepository.findByPost(post);
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
            return "redirect:/members/{me}/trade/buyList";
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

    //매너 디테일 지표
    @GetMapping("/manner/{memId}")
    public String toMannerDetail(@PathVariable String memId, Model model) {
        model.addAttribute("positiveMannerMap", reviewService.getPositiveMannerDetails(memId));
        model.addAttribute("negativeMannerMap", reviewService.getNegativeMannerDetails(memId));
        return  "review/mannerDetail";
    }

    //숨김 기능
    @PostMapping("/seller/hide")
    @ResponseBody
    public ResponseEntity<Map<String, String>> hideSellerReviewMessage(@RequestParam String reviewSellerId) {
        Map<String, String> resultMap = reviewSellerService.hideReviewSeller(Long.valueOf(reviewSellerId));
        if(resultMap.containsKey("fail")) {
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
    @PostMapping("/buyer/hide")
    @ResponseBody
    public ResponseEntity<Map<String, String>> hideBuyerReviewMessage(@RequestParam String reviewBuyerId) {
        Map<String, String> resultMap = reviewBuyerService.hideReviewBuyer(Long.valueOf(reviewBuyerId));
        if(resultMap.containsKey("fail")) {
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
