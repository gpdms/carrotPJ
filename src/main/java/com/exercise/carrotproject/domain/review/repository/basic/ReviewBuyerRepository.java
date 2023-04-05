package com.exercise.carrotproject.domain.review.repository.basic;

import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewBuyerRepository extends JpaRepository<ReviewBuyer, Long> {
}
