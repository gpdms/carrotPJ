package com.exercise.carrotproject.web.common.controller;

import com.exercise.carrotproject.domain.enumList.*;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.repository.TradeRepository;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerCustomRepositoryImpl;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerCustomRepositoryImpl;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewBuyerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewSellerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TestDBController {
    private final MemberService memberService;
    private final PostRepository postRepository;
    private final TradeRepository tradeRepository;

    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewBuyerCustomRepositoryImpl reviewBuyerCustomRepositoryImpl;
    private final ReviewSellerCustomRepositoryImpl reviewSellerCustomRepositoryImpl;
    private final ReviewSellerDetailRepository reviewSellerDetailRepository;
    private final ReviewBuyerDetailRepository reviewBuyerDetailRepository;

    @PersistenceContext
    EntityManager em;

    @GetMapping("td1")
    @ResponseBody
    @Transactional
    public void testDB(HttpServletRequest request) {
        Member mem1 = memberService.findOneMember("tester1");
        Member mem2 = memberService.findOneMember("tester2");
        Member mem3 = memberService.findOneMember("tester3");
        for(int i = 1 ; i <= 240; i++ ){
            if(i% 4  == 0 ){
                //mem1->mem2
                Post postBuild1 = Post.builder().title("글" + i).member(mem1).price(i * 1000).category(Category.ETC).loc(mem1.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
                Post post1 = postRepository.save(postBuild1);
                Trade trade = Trade.builder().post(post1).buyer(mem2).seller(mem1).hideStateBuyer(HideState.SHOW).build();
                tradeRepository.save(trade);
                if(i < 80) {
                    List<ReviewBuyerIndicator> bis= asList(ReviewBuyerIndicator.P1, ReviewBuyerIndicator.P2, ReviewBuyerIndicator.PB1);
                    ReviewBuyer save = reviewBuyerRepository.save(
                            ReviewBuyer.builder().seller(mem1).buyer(mem2).totalScore(ReviewBuyerIndicator.sumScore(bis)).post(post1)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BEST).build()
                    );
                    String sql = "INSERT INTO review_buyer_detail (review_buyer_id, buyer_id, review_buyer_indicator) " +
                            " VALUES ( :reviewBuyerId, 'tester2', :review_buyer_indicator) ";
                    Query query = em.createNativeQuery(sql);
                    for (ReviewBuyerIndicator bi : bis) {
                        query.setParameter("reviewBuyerId",  save.getReviewBuyerId());
                        query.setParameter("review_buyer_indicator",  bi.name());
                        query.getResultList();
                    }
                } else if (i >= 80 && i < 160) {
                    List<ReviewBuyerIndicator> bis= asList(ReviewBuyerIndicator.P2, ReviewBuyerIndicator.PB1);
                    ReviewBuyer save = reviewBuyerRepository.save(
                            ReviewBuyer.builder().seller(mem1).buyer(mem2).totalScore(ReviewBuyerIndicator.sumScore(bis)).post(post1)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.GOOD).build()
                    );
                    String sql = "INSERT INTO review_buyer_detail (review_buyer_id, buyer_id, review_buyer_indicator) " +
                            " VALUES ( :reviewBuyerId, 'tester2', :review_buyer_indicator) ";
                    Query query = em.createNativeQuery(sql);
                    for (ReviewBuyerIndicator  bi : bis) {
                        query.setParameter("reviewBuyerId",  save.getReviewBuyerId());
                        query.setParameter("review_buyer_indicator",  bi.name());
                        query.getResultList();
                    }
                } else {
                    List<ReviewBuyerIndicator> bis= asList(ReviewBuyerIndicator.N2, ReviewBuyerIndicator.N4,
                            ReviewBuyerIndicator.N6, ReviewBuyerIndicator.N8, ReviewBuyerIndicator.NB1);
                    ReviewBuyer save = reviewBuyerRepository.save(
                            ReviewBuyer.builder().seller(mem1).buyer(mem2).totalScore(ReviewBuyerIndicator.sumScore(bis)).post(post1)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BAD).build()
                    );
                    String sql = "INSERT INTO review_buyer_detail (review_buyer_id, buyer_id, review_buyer_indicator) " +
                            " VALUES ( :reviewBuyerId, 'tester2', :review_buyer_indicator) ";
                    Query query = em.createNativeQuery(sql);
                    for (ReviewBuyerIndicator  bi : bis) {
                        query.setParameter("reviewBuyerId",  save.getReviewBuyerId());
                        query.setParameter("review_buyer_indicator",  bi.name());
                        query.getResultList();
                    }
                }
            } else if(i% 4  == 1 ) {
                //mem2->mem3
                Post postBuild2 = Post.builder().title("글" + i).member(mem2).price(i * 1000).category(Category.DIGITAL_DEVICE).loc(mem2.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
                Post post2 = postRepository.save(postBuild2);
                Trade trade = Trade.builder().post(post2).buyer(mem3).seller(mem2).hideStateBuyer(HideState.SHOW).build();
                tradeRepository.save(trade);
                //리뷰
                if(i < 120) {
                    List<ReviewBuyerIndicator> bis= asList(ReviewBuyerIndicator.P1, ReviewBuyerIndicator.P2, ReviewBuyerIndicator.PB1);
                    ReviewBuyer save = reviewBuyerRepository.save(
                            ReviewBuyer.builder().seller(mem2).buyer(mem3).totalScore(ReviewBuyerIndicator.sumScore(bis)).post(post2)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BEST).build()
                    );
                    String sql = "INSERT INTO review_buyer_detail (review_buyer_id, buyer_id, review_buyer_indicator) " +
                            " VALUES ( :reviewBuyerId, 'tester3', :review_buyer_indicator) ";
                    Query query = em.createNativeQuery(sql);
                    for (ReviewBuyerIndicator  bi : bis) {
                        query.setParameter("reviewBuyerId",  save.getReviewBuyerId());
                        query.setParameter("review_buyer_indicator",  bi.name());
                        query.getResultList();
                    }

                    List<ReviewSellerIndicator> sis= asList(ReviewSellerIndicator.P2, ReviewSellerIndicator.PS2, ReviewSellerIndicator.PS4);
                    ReviewSeller save2 = reviewSellerRepository.save(
                            ReviewSeller.builder().seller(mem2).buyer(mem3).totalScore(ReviewSellerIndicator.sumScore(sis)).post(post2)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BEST).build()
                    );
                    String sql2 = "INSERT INTO review_seller_detail (review_seller_id, seller_id, review_seller_indicator) " +
                            " VALUES ( :reviewSellerId, 'tester2', :review_seller_indicator) ";
                    Query query2 = em.createNativeQuery(sql2);
                    for (ReviewSellerIndicator si : sis) {
                        query2.setParameter("reviewSellerId",  save2.getReviewSellerId());
                        query2.setParameter("review_seller_indicator",  si.name());
                        query2.getResultList();
                    }
                } else if (i >=120) {
                    List<ReviewBuyerIndicator> bis = asList(ReviewBuyerIndicator.P2, ReviewBuyerIndicator.PB1);
                    ReviewBuyer save = reviewBuyerRepository.save(
                            ReviewBuyer.builder().seller(mem2).buyer(mem3).totalScore(ReviewBuyerIndicator.sumScore(bis)).post(post2)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.GOOD).build()
                    );
                    String sql = "INSERT INTO review_buyer_detail (review_buyer_id, buyer_id, review_buyer_indicator) " +
                            " VALUES ( :reviewBuyerId, 'tester3', :review_buyer_indicator) ";
                    Query query = em.createNativeQuery(sql);
                    for (ReviewBuyerIndicator  bi : bis) {
                        query.setParameter("reviewBuyerId",  save.getReviewBuyerId());
                        query.setParameter("review_buyer_indicator", bi.name());
                        query.getResultList();
                    }

                    List<ReviewSellerIndicator> sis= asList(ReviewSellerIndicator.P1, ReviewSellerIndicator.PS1, ReviewSellerIndicator.PS2, ReviewSellerIndicator.PS3);
                    ReviewSeller save2 = reviewSellerRepository.save(
                            ReviewSeller.builder().seller(mem2).buyer(mem3).totalScore(ReviewSellerIndicator.sumScore(sis)).post(post2)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.GOOD).build()
                    );
                    String sql2 = "INSERT INTO review_seller_detail (review_seller_id, seller_id, review_seller_indicator) " +
                            " VALUES ( :reviewSellerId, 'tester2', :review_seller_indicator) ";
                    Query query2 = em.createNativeQuery(sql2);
                    for (ReviewSellerIndicator si : sis) {
                        query2.setParameter("reviewSellerId", save2.getReviewSellerId());
                        query2.setParameter("review_seller_indicator",  si.name());
                        query2.getResultList();
                    }
                }
            } else if(i% 4  == 2 ) {
                //mem3->mem2
                Post postBuild3 = Post.builder().title("글" + i).member(mem3).price(i * 1000).category(Category.BOOK).loc(mem3.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
                Post post2 = postRepository.save(postBuild3);
                Trade trade = Trade.builder().post(post2).buyer(mem1).seller(mem2).hideStateBuyer(HideState.SHOW).build();
                tradeRepository.save(trade);
            } else {
                //mem2->mem1
                Post postBuild2 = Post.builder().title("글" + i).member(mem2).price(i * 1000).category(Category.FOOD).loc(mem2.getLoc()).hideState(HideState.SHOW).sellState(SellState.SOLD).content("내용" + i).build();
                Post post2 = postRepository.save(postBuild2);
                Trade trade = Trade.builder().post(post2).buyer(mem1).seller(mem2).hideStateBuyer(HideState.SHOW).build();
                tradeRepository.save(trade);
            }
        }
    }

    @GetMapping("td2")
    @ResponseBody
    @Transactional
    public void testDB2 () {
        Member mem1 = memberService.findOneMember("tester1");
        Member mem2 = memberService.findOneMember("tester2");
        Member mem3 = memberService.findOneMember("tester3");
        for(int i = 1 ; i <= 240; i++ ){
            if(i%4 == 2) {
                Post post3 = postRepository.findById(Long.valueOf(i)).orElseThrow();
                if (i < 120) {
                    //mem3->mem2
                    List<ReviewBuyerIndicator> bis = asList(ReviewBuyerIndicator.N1, ReviewBuyerIndicator.N3, ReviewBuyerIndicator.N5, ReviewBuyerIndicator.NB1);
                    ReviewBuyer save = reviewBuyerRepository.save(
                            ReviewBuyer.builder().seller(mem3).buyer(mem2).totalScore(ReviewBuyerIndicator.sumScore(bis)).post(post3)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BAD).build()
                    );
                    String sql = "INSERT INTO review_buyer_detail (review_buyer_id, buyer_id, review_buyer_indicator) " +
                            " VALUES ( :reviewBuyerId, 'tester2', :review_buyer_indicator) ";
                    Query query = em.createNativeQuery(sql);
                    for (ReviewBuyerIndicator bi : bis) {
                        query.setParameter("reviewBuyerId", save.getReviewBuyerId());
                        query.setParameter("review_buyer_indicator", bi.name());
                        query.getResultList();
                    }

                    List<ReviewSellerIndicator> sis = asList(ReviewSellerIndicator.N1, ReviewSellerIndicator.N3, ReviewSellerIndicator.N5, ReviewSellerIndicator.N9,ReviewSellerIndicator.N6, ReviewSellerIndicator.N2);
                    ReviewSeller save2 = reviewSellerRepository.save(
                            ReviewSeller.builder().seller(mem3).buyer(mem2).totalScore(ReviewSellerIndicator.sumScore(sis)).post(post3)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BAD).build()
                    );
                    String sql2 = "INSERT INTO review_seller_detail (review_seller_id, seller_id, review_seller_indicator) " +
                            " VALUES ( :reviewSellerId, 'tester3', :review_seller_indicator) ";
                    Query query2 = em.createNativeQuery(sql2);
                    for (ReviewSellerIndicator si : sis) {
                        query2.setParameter("reviewSellerId", save2.getReviewSellerId());
                        query2.setParameter("review_seller_indicator", si.name());
                        query2.getResultList();
                    }
                } else if (i >= 120) {
                    List<ReviewBuyerIndicator> bis = asList(ReviewBuyerIndicator.PB1, ReviewBuyerIndicator.P2,
                            ReviewBuyerIndicator.P3);
                    ReviewBuyer save = reviewBuyerRepository.save(
                            ReviewBuyer.builder().seller(mem3).buyer(mem2).totalScore(ReviewBuyerIndicator.sumScore(bis)).post(post3)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.GOOD).build()
                    );
                    String sql = "INSERT INTO review_buyer_detail (review_buyer_id, buyer_id, review_buyer_indicator) " +
                            " VALUES ( :reviewBuyerId, 'tester2', :review_buyer_indicator) ";
                    Query query = em.createNativeQuery(sql);
                    for (ReviewBuyerIndicator bi : bis) {
                        query.setParameter("reviewBuyerId", save.getReviewBuyerId());
                        query.setParameter("review_buyer_indicator", bi.name());
                        query.getResultList();
                    }

                    List<ReviewSellerIndicator> sis = asList(ReviewSellerIndicator.P2,ReviewSellerIndicator.P3,ReviewSellerIndicator.PS2,ReviewSellerIndicator.PS4, ReviewSellerIndicator.P1);
                    ReviewSeller save2 = reviewSellerRepository.save(
                            ReviewSeller.builder().seller(mem3).buyer(mem2).totalScore(ReviewSellerIndicator.sumScore(sis)).post(post3)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.GOOD).build()
                    );
                    String sql2 = "INSERT INTO review_seller_detail (review_seller_id, seller_id, review_seller_indicator) " +
                            " VALUES ( :reviewSellerId, 'tester3', :review_seller_indicator) ";
                    Query query2 = em.createNativeQuery(sql2);
                    for (ReviewSellerIndicator si : sis) {
                        query2.setParameter("reviewSellerId", save2.getReviewSellerId());
                        query2.setParameter("review_seller_indicator", si.name());
                        query2.getResultList();
                    }
                }
            } else if(i%4 == 3) {
                Post post4 = postRepository.findById(Long.valueOf(i)).orElseThrow();
                if (i < 120) {
                    //mem2->mem1
                    List<ReviewBuyerIndicator> bis = Arrays.stream(ReviewBuyerIndicator.values())
                            .filter(indicator -> indicator.name().contains("P") && !indicator.name().contains("B"))
                            .collect(Collectors.toList());
                    ReviewBuyer save = reviewBuyerRepository.save(
                            ReviewBuyer.builder().seller(mem2).buyer(mem1).totalScore(ReviewBuyerIndicator.sumScore(bis)).post(post4)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BEST).build()
                    );
                    String sql = "INSERT INTO review_buyer_detail (review_buyer_id, buyer_id, review_buyer_indicator) " +
                            " VALUES ( :reviewBuyerId, 'tester1', :review_buyer_indicator) ";
                    Query query = em.createNativeQuery(sql);
                    for (ReviewBuyerIndicator bi : bis) {
                        query.setParameter("reviewBuyerId", save.getReviewBuyerId());
                        query.setParameter("review_buyer_indicator", bi.name());
                        query.getResultList();
                    }

                    List<ReviewSellerIndicator> sis = Arrays.stream(ReviewSellerIndicator.values())
                            .filter(indicator -> indicator.name().contains("N"))
                            .collect(Collectors.toList());
                    ReviewSeller save2 = reviewSellerRepository.save(
                            ReviewSeller.builder().seller(mem2).buyer(mem1).totalScore(ReviewSellerIndicator.sumScore(sis)).post(post4)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BAD).build()
                    );
                    String sql2 = "INSERT INTO review_seller_detail (review_seller_id, seller_id, review_seller_indicator) " +
                            " VALUES ( :reviewSellerId, 'tester2', :review_seller_indicator) ";
                    Query query2 = em.createNativeQuery(sql2);
                    for (ReviewSellerIndicator si : sis) {
                        query2.setParameter("reviewSellerId", save2.getReviewSellerId());
                        query2.setParameter("review_seller_indicator", si.name());
                        query2.getResultList();
                    }
                } else if (i >= 120) {
                    List<ReviewBuyerIndicator> bis = Arrays.stream(ReviewBuyerIndicator.values())
                            .filter(indicator -> indicator.name().contains("N"))
                            .collect(Collectors.toList());
                    ReviewBuyer save = reviewBuyerRepository.save(
                            ReviewBuyer.builder().seller(mem1).buyer(mem2).totalScore(ReviewBuyerIndicator.sumScore(bis)).post(post4)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BAD).build()
                    );
                    String sql = "INSERT INTO review_buyer_detail (review_buyer_id, buyer_id, review_buyer_indicator) " +
                            " VALUES ( :reviewBuyerId, 'tester2', :review_buyer_indicator) ";
                    Query query = em.createNativeQuery(sql);
                    for (ReviewBuyerIndicator bi : bis) {
                        query.setParameter("reviewBuyerId", save.getReviewBuyerId());
                        query.setParameter("review_buyer_indicator", bi.name());
                        query.getResultList();
                    }

                    List<ReviewSellerIndicator> sis =  Arrays.stream(ReviewSellerIndicator.values())
                            .collect(Collectors.toList());
                    ReviewSeller save2 = reviewSellerRepository.save(
                            ReviewSeller.builder().seller(mem1).buyer(mem2).totalScore(ReviewSellerIndicator.sumScore(sis)).post(post4)
                                    .hideState(HideState.SHOW).reviewState(ReviewState.BEST).build()
                    );
                    String sql2 = "INSERT INTO review_seller_detail (review_seller_id, seller_id, review_seller_indicator) " +
                            " VALUES ( :reviewSellerId, 'tester1', :review_seller_indicator) ";
                    Query query2 = em.createNativeQuery(sql2);
                    for (ReviewSellerIndicator si : sis) {
                        query2.setParameter("reviewSellerId", save2.getReviewSellerId());
                        query2.setParameter("review_seller_indicator", si.name());
                        query2.getResultList();
                    }
                }


            }
        }

    }
}
