package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerCustomRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerCustomRepositoryImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.Optional;

public interface ReviewBuyerRepository extends JpaRepository<ReviewBuyer, Long>, ReviewBuyerCustomRepository {
 void deleteByPost(Post post);
 boolean existsByPostPostId(Long postId);
 boolean existsByReviewBuyerIdLessThan(Long reviewBuyerId);
 boolean existsByCreatedTimeBefore(Timestamp timestamp);
}
