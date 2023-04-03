package com.exercise.carrotproject.web.review;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.domain.post.entity.BuyList;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.SellList;
import com.exercise.carrotproject.domain.post.repository.BuyListRepository;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.repository.SellListRepository;
import com.exercise.carrotproject.domain.review.dto.ReviewBuyerDetailDto;
import com.exercise.carrotproject.domain.review.service.ReviewServiceImpl;
import com.exercise.carrotproject.web.common.SessionConst;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Slf4j
@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final MemberServiceImpl memberService;
    private final ReviewServiceImpl reviewServiceRepository;
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    //임시 필드
    private final PostRepository postRepository;
    private final BuyListRepository buyListRepository;
    private final SellListRepository sellListRepository;
    //테스트 db
   @GetMapping()
   @ResponseBody
   public void toReviewBuyerForm(HttpServletRequest request) {

       Member seller1 = memberService.findOneMember("tester2").orElse(null);
       Member seller2 = memberService.findOneMember("tester3").orElse(null);

       HttpSession session = request.getSession();
       MemberDto loginMember =  (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
       Member buyer = memberService.findOneMember(loginMember.getMemId()).orElse(null);

       for(int i = 1 ; i <= 10; i++ ){
           if(i% 2  == 0 ){
               Post postBuild = Post.builder().title("글" + i).member(seller1).price(i * 1000).category(Category.ETC).loc(seller1.getLoc()).hideState(0).sellState(2).content("내용" + i).build();
               Post post1 = postRepository.save(postBuild);
               BuyList buyList= BuyList.builder().post(post1).buyer(buyer).seller(seller1).build();
               buyListRepository.save(buyList);
           }
           Post postBuild = Post.builder().title("글" + i).member(seller2).price(i * 1000).category(Category.DIGITAL_DEVICE).loc(seller2.getLoc()).hideState(0).sellState(2).content("내용" + i).build();
           Post post2 = postRepository.save(postBuild);
           SellList sellList = SellList.builder().post(post2).buyer(buyer).seller(seller2).build();
           sellListRepository.save(sellList);
       }

   }

   @GetMapping("/buyer/add")
   public String toBuyerReviewForm(@ModelAttribute ReviewBuyerDetailDto reviewBuyerDetailDto){
       return "review/buyerReviewForm";
   }
}
