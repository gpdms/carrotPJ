package com.exercise.carrotproject.domain.review.repository.basic;

import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewBuyerRepository extends JpaRepository<ReviewBuyer, Long> {

 ReviewBuyer findByPost(Post post);
 void deleteByPost(Post post);
}
