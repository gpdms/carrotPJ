package com.exercise.carrotproject.web.review.controller;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.domain.post.entity.BuyList;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.SellList;
import com.exercise.carrotproject.domain.post.repository.BuyListRepository;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.repository.SellListRepository;
import com.exercise.carrotproject.domain.post.service.PostService;
import com.exercise.carrotproject.domain.post.service.PostServiceImpl;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.service.ReviewServiceImpl;
import com.exercise.carrotproject.web.common.SessionConst;
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


@Slf4j
@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final MemberServiceImpl memberService;
    private final ReviewServiceImpl reviewService;

    private final PostRepository postRepository;
    private final BuyListRepository buyListRepository;
    private final SellListRepository sellListRepository;

    //테스트 db
   @GetMapping
   @ResponseBody
   public void toReviewBuyerForm(HttpServletRequest request) {
       Member seller1 = memberService.findOneMember("tester2").orElse(null);
       Member seller2 = memberService.findOneMember("tester3").orElse(null);
       Member buyer = memberService.findOneMember("tester1").orElse(null);

       for(int i = 1 ; i <= 10; i++ ){
           if(i% 2  == 0 ){
               Post postBuild1 = Post.builder().title("글" + i).member(seller1).price(i * 1000).category(Category.ETC).loc(seller1.getLoc()).hideState(0).sellState(2).content("내용" + i).build();
               Post post1 = postRepository.save(postBuild1);
               BuyList buyList= BuyList.builder().post(post1).buyer(buyer).seller(seller1).build();
               buyListRepository.save(buyList);
               SellList sellList = SellList.builder().post(post1).buyer(buyer).seller(seller1).build();
               sellListRepository.save(sellList);
           } else {
               Post postBuild2 = Post.builder().title("글" + i).member(seller2).price(i * 1000).category(Category.DIGITAL_DEVICE).loc(seller2.getLoc()).hideState(0).sellState(2).content("내용" + i).build();
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
        log.info("reviewSellerForm----{}", reviewForm);
        ReviewSeller reviewSeller = ReviewSeller.builder()
                .seller(memberService.findOneMember(reviewForm.getSellerId()).orElse(null))
                .buyer(memberService.findOneMember(reviewForm.getBuyerId()).orElse(null))
                .post(postRepository.findById(reviewForm.getPostId()).orElse(null))
                .reviewState(ReviewState.findByStateCode(reviewForm.getReviewStateCode()))
                .message(reviewForm.getMessage())
                .build();
        List<ReviewSellerIndicator> indicatorList = ReviewSellerIndicator.findAllByEnumName(reviewForm.getIndicators());
        log.info("indicatorList --- {}", indicatorList);
        return "review/reviewForm";
    }

   @GetMapping("/buyer")
   public String toBuyerReviewForm(
           HttpSession session,
           @RequestParam String postId,
           @ModelAttribute("reviewForm") ReviewForm reviewForm){
       MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
       SellList sellOne = sellListRepository.findByPost(postRepository.findById(Long.valueOf(postId)).orElseThrow());
       reviewForm = ReviewForm.builder().sellerId(loginMember.getMemId())
               .buyerId(sellOne.getBuyer().getMemId())
               .postId(Long.valueOf(postId))
               .build();
       return "review/reviewForm";
   }
    @PostMapping("/buyer")
    @ResponseBody
    public String addBuyerReview(@RequestBody ReviewForm reviewForm) {
        log.info("reviewBuyerForm----{}", reviewForm);
        return "review/reviewForm";
    }


}
