package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;

public interface ReviewSellerRepository extends JpaRepository<ReviewSeller, Long>, ReviewSellerCustomRepository{
    void deleteByPost(Post post);
    boolean existsByPostPostId(Long postId);
    boolean existsByReviewSellerIdLessThan(Long reviewSellerId);
    boolean existsByCreatedTimeBefore(Timestamp timestamp);
}
