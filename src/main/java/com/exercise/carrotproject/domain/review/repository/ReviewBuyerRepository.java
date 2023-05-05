package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerCustomRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerCustomRepositoryImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewBuyerRepository extends JpaRepository<ReviewBuyer, Long>, ReviewBuyerCustomRepository {
 ReviewBuyer findByPost(Post post);
 void deleteByPost(Post post);
}
