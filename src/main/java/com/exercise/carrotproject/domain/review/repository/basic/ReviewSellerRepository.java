package com.exercise.carrotproject.domain.review.repository.basic;

import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSellerRepository extends JpaRepository<ReviewSeller, Long> {
}
