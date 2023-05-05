package com.exercise.carrotproject.domain.review.repository.detail;

import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyerDetail;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewBuyerDetailRepository extends JpaRepository<ReviewBuyerDetail, Long> {
    List<ReviewBuyerDetail> findByReviewBuyer(ReviewBuyer reviewBuyer);
}
